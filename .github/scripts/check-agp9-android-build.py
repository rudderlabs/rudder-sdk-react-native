#!/usr/bin/env python3
"""Verify Kotlin Android library modules compile and pass unit tests under AGP 9.

Runs two temporary Gradle 9.4.1 / AGP 9.2.1 consumer builds against every Kotlin
Android module under libs/:
1. AGP 9 defaults (android.builtInKotlin=true, android.newDsl=true), asserting
   that AGP provides the `kotlin` extension and `kotlin-android` is not applied.
2. AGP 9 legacy opt-out (android.builtInKotlin=false, android.newDsl=false),
   asserting that `kotlin-android` is applied.

In both modes, injects a temporary canary `.kt` file into each module's
`src/main/oldarch` directory (inside the temporary workspace only) and asserts
that it compiles, preventing regressions where AGP 9 built-in Kotlin silently
ignores `java.srcDirs`.
"""

from __future__ import annotations

from dataclasses import dataclass
from pathlib import Path
import re
import shutil
import stat
import subprocess
import tempfile

AGP_VERSION = "9.2.1"
GRADLE_VERSION = "9.4.1"
GRADLE_SHA256 = "2ab2958f2a1e51120c326cad6f385153bb11ee93b3c216c5fccebfdfbb7ec6cb"
KOTLIN_VERSION = "2.2.10"
REACT_ANDROID_VERSION = "0.83.9"

REQUIRED_MODULES = {
    "libs/sdk/android",
    "libs/rudder-integration-firebase-react-native/android",
    "libs/rudder-integration-sprig-react-native/android",
}

SDK_GRADLE_PROJECT = "rudderstack_rudder-sdk-react-native"
CANARY_CLASS = "Agp9OldArchKotlinCanary"


@dataclass(frozen=True)
class KotlinAndroidModule:
    rel_dir: str
    gradle_project: str
    has_oldarch: bool
    has_newarch: bool


def strip_groovy_comments(text: str) -> str:
    """Remove Groovy // line comments and /* */ block comments while preserving string literals."""
    out: list[str] = []
    i = 0
    n = len(text)
    while i < n:
        ch = text[i]
        nxt = text[i + 1] if i + 1 < n else ""

        # Line comment: // ... (preserve newline so line-based regexes stay accurate)
        if ch == "/" and nxt == "/":
            i += 2
            while i < n and text[i] != "\n":
                i += 1
            continue

        # Block comment: /* ... */ (replace with space/newlines to preserve token boundaries)
        if ch == "/" and nxt == "*":
            i += 2
            out.append(" ")
            while i < n:
                if text[i] == "*" and i + 1 < n and text[i + 1] == "/":
                    i += 2
                    break
                if text[i] == "\n":
                    out.append("\n")
                i += 1
            continue

        # Triple-quoted string literals ('''...''' or """...""")
        if ch in ("'", '"') and text[i : i + 3] == ch * 3:
            quote = ch * 3
            out.append(quote)
            i += 3
            while i < n:
                if text[i] == "\\" and i + 1 < n:
                    out.append(text[i : i + 2])
                    i += 2
                    continue
                if text[i : i + 3] == quote:
                    out.append(quote)
                    i += 3
                    break
                out.append(text[i])
                i += 1
            continue

        # Single- or double-quoted string literals
        if ch in ("'", '"'):
            quote = ch
            out.append(quote)
            i += 1
            while i < n:
                if text[i] == "\\" and i + 1 < n:
                    out.append(text[i : i + 2])
                    i += 2
                    continue
                out.append(text[i])
                if text[i] == quote or text[i] == "\n":
                    i += 1
                    break
                i += 1
            continue

        out.append(ch)
        i += 1

    return "".join(out)


def discover_kotlin_android_modules(repo_root: Path) -> list[KotlinAndroidModule]:
    modules: list[KotlinAndroidModule] = []
    libs_root = repo_root / "libs"

    for build_gradle in sorted(libs_root.rglob("android/build.gradle")):
        rel_parts = build_gradle.relative_to(repo_root).parts
        if any(part in {"node_modules", "build", "dist"} for part in rel_parts):
            continue

        android_dir = build_gradle.parent
        text = strip_groovy_comments(build_gradle.read_text(encoding="utf-8"))
        has_kt_files = any(android_dir.rglob("*.kt"))
        references_kotlin = bool(
            re.search(r"\bkotlin-android\b|\bkotlin\.srcDirs\b|\bkotlin\.directories\b", text)
        )
        if not (has_kt_files or references_kotlin):
            continue

        rel_dir = android_dir.relative_to(repo_root).as_posix()
        gradle_project = (
            SDK_GRADLE_PROJECT if rel_dir == "libs/sdk/android" else android_dir.parent.name
        )
        has_oldarch = "src/main/oldarch" in text
        has_newarch = "src/main/newarch" in text
        modules.append(
            KotlinAndroidModule(
                rel_dir=rel_dir,
                gradle_project=gradle_project,
                has_oldarch=has_oldarch,
                has_newarch=has_newarch,
            )
        )

    discovered = {m.rel_dir for m in modules}
    missing = sorted(REQUIRED_MODULES - discovered)
    if missing:
        raise SystemExit(f"Expected Kotlin Android modules were not discovered: {missing}")

    return modules


def assert_kotlin_src_dirs_mirrored(repo_root: Path, modules: list[KotlinAndroidModule]) -> None:
    """Verify every module that registers newarch/oldarch mirrors it into kotlin sources."""
    for module in modules:
        build_gradle = repo_root / module.rel_dir / "build.gradle"
        text = strip_groovy_comments(build_gradle.read_text(encoding="utf-8"))
        for arch_dir, present in (
            ("src/main/newarch", module.has_newarch),
            ("src/main/oldarch", module.has_oldarch),
        ):
            if not present:
                continue
            pattern = re.compile(
                rf"kotlin\.(?:srcDirs|directories|srcDir)\b[^\n]*{re.escape(arch_dir)}"
            )
            if not pattern.search(text):
                raise SystemExit(
                    f"{module.rel_dir}/build.gradle adds {arch_dir} to java sources "
                    f"but does not mirror it into kotlin sources"
                )


def copy_wrapper(repo_root: Path, work_dir: Path) -> Path:
    src_gradlew = repo_root / "apps/example/android/gradlew"
    src_jar = repo_root / "apps/example/android/gradle/wrapper/gradle-wrapper.jar"
    if not src_gradlew.is_file() or not src_jar.is_file():
        raise SystemExit("Repository Gradle wrapper files are missing under apps/example/android")

    dst_gradlew = work_dir / "gradlew"
    dst_wrapper_dir = work_dir / "gradle/wrapper"
    dst_wrapper_dir.mkdir(parents=True, exist_ok=True)
    shutil.copy2(src_gradlew, dst_gradlew)
    dst_gradlew.chmod(dst_gradlew.stat().st_mode | stat.S_IXUSR | stat.S_IXGRP | stat.S_IXOTH)
    shutil.copy2(src_jar, dst_wrapper_dir / "gradle-wrapper.jar")

    (dst_wrapper_dir / "gradle-wrapper.properties").write_text(
        "\n".join(
            [
                "distributionBase=GRADLE_USER_HOME",
                "distributionPath=wrapper/dists",
                f"distributionUrl=https\\://services.gradle.org/distributions/gradle-{GRADLE_VERSION}-bin.zip",
                f"distributionSha256Sum={GRADLE_SHA256}",
                "networkTimeout=10000",
                "validateDistributionUrl=true",
                "zipStoreBase=GRADLE_USER_HOME",
                "zipStorePath=wrapper/dists",
                "",
            ]
        ),
        encoding="utf-8",
    )
    return dst_gradlew


def run_scenario(
    repo_root: Path,
    modules: list[KotlinAndroidModule],
    scenario_name: str,
    built_in_kotlin: bool,
    new_dsl: bool,
) -> None:
    print(
        f"\n=== Scenario: {scenario_name} "
        f"(AGP={AGP_VERSION}, Gradle={GRADLE_VERSION}, "
        f"builtInKotlin={built_in_kotlin}, newDsl={new_dsl}) ===",
        flush=True,
    )

    with tempfile.TemporaryDirectory(prefix=f"{scenario_name}-") as tmp:
        work_dir = Path(tmp)
        gradlew = copy_wrapper(repo_root, work_dir)

        include_lines = []
        canary_modules: list[KotlinAndroidModule] = []
        for idx, module in enumerate(modules):
            src_android = repo_root / module.rel_dir
            dst_android = work_dir / module.rel_dir
            shutil.copytree(
                src_android,
                dst_android,
                ignore=shutil.ignore_patterns("build", ".gradle"),
            )
            include_lines.append(f"include ':{module.gradle_project}'")
            include_lines.append(
                f"project(':{module.gradle_project}').projectDir = file('{module.rel_dir}')"
            )

            if module.has_oldarch:
                canary_dir = dst_android / f"src/main/oldarch/java/com/rudderstack/agp9canary{idx}"
                canary_dir.mkdir(parents=True, exist_ok=True)
                (canary_dir / f"{CANARY_CLASS}.kt").write_text(
                    f"package com.rudderstack.agp9canary{idx}\n\n"
                    f"internal object {CANARY_CLASS} {{\n"
                    f'  const val STATUS = "ok"\n'
                    f"}}\n",
                    encoding="utf-8",
                )
                canary_modules.append(module)

        (work_dir / "settings.gradle").write_text(
            "\n".join(
                [
                    "pluginManagement {",
                    "  repositories {",
                    "    google()",
                    "    mavenCentral()",
                    "    gradlePluginPortal()",
                    "  }",
                    "}",
                    f'rootProject.name = "agp9-regression-{scenario_name}"',
                    *include_lines,
                    "",
                ]
            ),
            encoding="utf-8",
        )

        expect_legacy_plugin = "false" if built_in_kotlin else "true"
        (work_dir / "build.gradle").write_text(
            f"""buildscript {{
  ext {{
    compileSdkVersion = 36
    minSdkVersion = 24
    targetSdkVersion = 36
    kotlinVersion = "{KOTLIN_VERSION}"
  }}
  repositories {{
    google()
    mavenCentral()
  }}
  dependencies {{
    classpath "com.android.tools.build:gradle:{AGP_VERSION}"
    classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:{KOTLIN_VERSION}"
  }}
}}

subprojects {{
  repositories {{
    google()
    mavenCentral()
  }}
  afterEvaluate {{
    if (plugins.hasPlugin("com.android.library")) {{
      // Mirror com.facebook.react.rootproject Java 17 alignment on RN 0.73+.
      android.compileOptions {{
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
      }}

      def kotlinExt = extensions.findByName("kotlin")
      if (kotlinExt == null) {{
        throw new GradleException("Expected kotlin extension to be registered on project ${{project.path}}")
      }}
      def hasLegacyKotlinPlugin = plugins.hasPlugin("kotlin-android") || plugins.hasPlugin("org.jetbrains.kotlin.android")
      def expectLegacyKotlinPlugin = {expect_legacy_plugin}
      if (hasLegacyKotlinPlugin != expectLegacyKotlinPlugin) {{
        throw new GradleException(
          "Unexpected kotlin-android plugin state on ${{project.path}}: " +
          "hasLegacyKotlinPlugin=${{hasLegacyKotlinPlugin}}, expected=${{expectLegacyKotlinPlugin}}"
        )
      }}
    }}
  }}
  configurations.all {{
    resolutionStrategy.eachDependency {{ details ->
      if (details.requested.group == "com.facebook.react" && details.requested.name == "react-android") {{
        details.useVersion "{REACT_ANDROID_VERSION}"
      }}
    }}
  }}
}}
""",
            encoding="utf-8",
        )

        (work_dir / "gradle.properties").write_text(
            "\n".join(
                [
                    "org.gradle.jvmargs=-Xmx2048m",
                    "android.useAndroidX=true",
                    f"android.builtInKotlin={str(built_in_kotlin).lower()}",
                    f"android.newDsl={str(new_dsl).lower()}",
                    "",
                ]
            ),
            encoding="utf-8",
        )

        tasks: list[str] = []
        for module in modules:
            tasks.append(f":{module.gradle_project}:compileDebugKotlin")
            tasks.append(f":{module.gradle_project}:compileDebugJavaWithJavac")
        tasks.append(f":{SDK_GRADLE_PROJECT}:testDebugUnitTest")

        subprocess.run(
            [str(gradlew), "--no-daemon", "--stacktrace", *tasks],
            cwd=work_dir,
            check=True,
        )

        for module in canary_modules:
            build_dir = work_dir / module.rel_dir / "build"
            canary_classes = list(build_dir.rglob(f"{CANARY_CLASS}.class"))
            if not canary_classes:
                raise SystemExit(
                    f"Canary Kotlin file in {module.rel_dir}/src/main/oldarch was not compiled "
                    f"in scenario {scenario_name}"
                )

    print(f"PASSED: {scenario_name} ({len(modules)} Kotlin modules verified)", flush=True)


def main() -> None:
    repo_root = Path(__file__).resolve().parents[2]
    modules = discover_kotlin_android_modules(repo_root)
    print(
        f"Discovered {len(modules)} Kotlin Android modules: "
        + ", ".join(m.rel_dir for m in modules),
        flush=True,
    )
    assert_kotlin_src_dirs_mirrored(repo_root, modules)

    run_scenario(
        repo_root=repo_root,
        modules=modules,
        scenario_name="agp9-built-in-kotlin",
        built_in_kotlin=True,
        new_dsl=True,
    )
    run_scenario(
        repo_root=repo_root,
        modules=modules,
        scenario_name="agp9-opt-out",
        built_in_kotlin=False,
        new_dsl=False,
    )


if __name__ == "__main__":
    main()
