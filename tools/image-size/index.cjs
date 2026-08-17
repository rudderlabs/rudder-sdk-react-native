'use strict';

const fs = require('fs');

const supportedTypes = ['png', 'jpg', 'gif', 'webp', 'bmp', 'svg'];

function toBuffer(input) {
  if (Buffer.isBuffer(input)) {
    return input;
  }
  if (typeof input === 'string') {
    return fs.readFileSync(input);
  }
  if (input instanceof Uint8Array) {
    return Buffer.from(input.buffer, input.byteOffset, input.byteLength);
  }
  throw new TypeError('Expected a file path, Buffer, or Uint8Array');
}

function readUInt24LE(buffer, offset) {
  return buffer[offset] + (buffer[offset + 1] << 8) + (buffer[offset + 2] << 16);
}

function parsePng(buffer) {
  if (
    buffer.length >= 24 &&
    buffer[0] === 0x89 &&
    buffer.toString('ascii', 1, 4) === 'PNG' &&
    buffer.toString('ascii', 12, 16) === 'IHDR'
  ) {
    return {
      width: buffer.readUInt32BE(16),
      height: buffer.readUInt32BE(20),
      type: 'png',
    };
  }
}

function parseGif(buffer) {
  if (
    buffer.length >= 10 &&
    (buffer.toString('ascii', 0, 6) === 'GIF87a' || buffer.toString('ascii', 0, 6) === 'GIF89a')
  ) {
    return {
      width: buffer.readUInt16LE(6),
      height: buffer.readUInt16LE(8),
      type: 'gif',
    };
  }
}

function parseBmp(buffer) {
  if (buffer.length >= 26 && buffer.toString('ascii', 0, 2) === 'BM') {
    return {
      width: Math.abs(buffer.readInt32LE(18)),
      height: Math.abs(buffer.readInt32LE(22)),
      type: 'bmp',
    };
  }
}

function parseJpeg(buffer) {
  if (buffer.length < 4 || buffer[0] !== 0xff || buffer[1] !== 0xd8) {
    return;
  }

  let offset = 2;
  while (offset + 3 < buffer.length) {
    while (offset < buffer.length && buffer[offset] === 0xff) {
      offset += 1;
    }

    if (offset >= buffer.length) {
      break;
    }

    const marker = buffer[offset];
    offset += 1;

    if (marker === 0xd9 || marker === 0xda) {
      break;
    }

    if ((marker >= 0xd0 && marker <= 0xd7) || marker === 0x01) {
      continue;
    }

    if (offset + 1 >= buffer.length) {
      break;
    }

    const segmentLength = buffer.readUInt16BE(offset);
    if (segmentLength < 2 || offset + segmentLength > buffer.length) {
      break;
    }

    if (
      marker === 0xc0 ||
      marker === 0xc1 ||
      marker === 0xc2 ||
      marker === 0xc3 ||
      marker === 0xc5 ||
      marker === 0xc6 ||
      marker === 0xc7 ||
      marker === 0xc9 ||
      marker === 0xca ||
      marker === 0xcb ||
      marker === 0xcd ||
      marker === 0xce ||
      marker === 0xcf
    ) {
      if (segmentLength >= 7) {
        return {
          height: buffer.readUInt16BE(offset + 3),
          width: buffer.readUInt16BE(offset + 5),
          type: 'jpg',
        };
      }
      break;
    }

    offset += segmentLength;
  }
}

function parseWebp(buffer) {
  if (
    buffer.length < 30 ||
    buffer.toString('ascii', 0, 4) !== 'RIFF' ||
    buffer.toString('ascii', 8, 12) !== 'WEBP'
  ) {
    return;
  }

  const chunkType = buffer.toString('ascii', 12, 16);
  if (chunkType === 'VP8X' && buffer.length >= 30) {
    return {
      width: readUInt24LE(buffer, 24) + 1,
      height: readUInt24LE(buffer, 27) + 1,
      type: 'webp',
    };
  }

  if (chunkType === 'VP8 ' && buffer.length >= 30) {
    return {
      width: buffer.readUInt16LE(26) & 0x3fff,
      height: buffer.readUInt16LE(28) & 0x3fff,
      type: 'webp',
    };
  }

  if (chunkType === 'VP8L' && buffer.length >= 25 && buffer[20] === 0x2f) {
    const bits = buffer.readUInt32LE(21);
    return {
      width: (bits & 0x3fff) + 1,
      height: ((bits >> 14) & 0x3fff) + 1,
      type: 'webp',
    };
  }
}

function parseSvg(buffer) {
  const text = buffer.toString('utf8', 0, Math.min(buffer.length, 4096));
  if (!/<svg[\s>]/i.test(text)) {
    return;
  }

  const widthMatch = text.match(/\bwidth=["']?([0-9.]+)/i);
  const heightMatch = text.match(/\bheight=["']?([0-9.]+)/i);
  if (widthMatch && heightMatch) {
    return {
      width: Number(widthMatch[1]),
      height: Number(heightMatch[1]),
      type: 'svg',
    };
  }

  const viewBoxMatch = text.match(/\bviewBox=["']?([0-9.\s-]+)["']?/i);
  if (viewBoxMatch) {
    const values = viewBoxMatch[1].trim().split(/\s+/).map(Number);
    if (values.length === 4 && values.every(Number.isFinite)) {
      return {
        width: values[2],
        height: values[3],
        type: 'svg',
      };
    }
  }
}

function imageSize(input) {
  const buffer = toBuffer(input);
  const result =
    parsePng(buffer) ||
    parseGif(buffer) ||
    parseBmp(buffer) ||
    parseJpeg(buffer) ||
    parseWebp(buffer) ||
    parseSvg(buffer);

  if (!result || !Number.isFinite(result.width) || !Number.isFinite(result.height)) {
    throw new TypeError('Unsupported image type');
  }

  return result;
}

function disableTypes() {}

module.exports = imageSize;
module.exports.default = imageSize;
module.exports.imageSize = imageSize;
module.exports.disableTypes = disableTypes;
module.exports.types = supportedTypes;
