import { logError } from './Logger';

function convertNestedObjects(obj: Record<string, unknown>): Record<string, unknown> {
  const updatedObj: Record<string, unknown> = {};

  for (const [key, val] of Object.entries(obj)) {
    if (shouldDropValue(val)) {
      continue;
    }
    if (val === null) {
      updatedObj[key] = null;
    } else if (isObject(val)) {
      if (!Array.isArray(val)) {
        updatedObj[key] = handleObjectValue(val);
      } else {
        updatedObj[key] = handleArrayValue(val);
      }
    } else {
      updatedObj[key] = val;
    }
  }

  return updatedObj;
}

function shouldDropValue(val: unknown): boolean {
  return typeof val === 'number' && isNaN(val as number);
}

function isObject(val: unknown): val is Record<string, unknown> {
  return val !== null && typeof val === 'object';
}

function handleObjectValue(val: Record<string, unknown>): Record<string, unknown> {
  return convertNestedObjects(val);
}

function handleArrayValue(val: unknown[]): unknown[] {
  const updatedArray: unknown[] = [];

  for (const item of val) {
    console.log('Abhishek: item: ', item);
    if (!isObject(item)) {
      if (shouldDropValue(item)) {
        continue;
      } else {
        updatedArray.push(item);
      }
    } else {
      updatedArray.push(convertNestedObjects(item as Record<string, unknown>));
    }
  }

  return updatedArray;
}

export function filterNaN(value: Record<string, unknown> | null): Record<string, unknown> | null {
  if (value === null) return null;
  let updatedObj: Record<string, unknown> = {};
  try {
    updatedObj = convertNestedObjects(value);
  } catch (error) {
    logError('An error occurred while filtering NaN values: ' + error);
  }
  return updatedObj;
}
