"""Fail if native dependency resolution would omit or replace a local library."""

import json
from pathlib import Path
import subprocess

root = Path(__file__).resolve().parents[2]
result = subprocess.run(
    ["node", str(root / "node_modules/react-native/cli.js"), "config"],
    cwd=root / "apps/example",
    check=True,
    capture_output=True,
    text=True,
)
linked = json.loads(result.stdout)["dependencies"]
expected = []
for manifest in (root / "libs").rglob("package.json"):
    if any(part in {"node_modules", "build", "dist"} for part in manifest.relative_to(root).parts):
        continue
    if not (manifest.parent / "android/build.gradle").is_file():
        continue
    name = json.loads(manifest.read_text())["name"]
    dependency = linked.get(name, {})
    if not dependency.get("platforms", {}).get("android"):
        raise SystemExit(f"Android autolinking omitted {name}")
    if Path(dependency["root"]).resolve() != manifest.parent.resolve():
        raise SystemExit(f"Consumer uses a published copy instead of local {name}")
    source_dir = dependency["platforms"]["android"].get("sourceDir")
    if not source_dir or Path(source_dir).resolve() != (manifest.parent / "android").resolve():
        raise SystemExit(f"Consumer uses a different Android source directory for {name}")
    expected.append(name)
if not expected:
    raise SystemExit("No local Android libraries were checked")
print(f"Verified {len(expected)} local Android libraries, including every integration")
