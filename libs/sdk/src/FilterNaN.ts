/* eslint-disable @typescript-eslint/no-explicit-any */
import { logError } from './Logger';

const isValueNaN = (val: any) => typeof val === 'number' && Number.isNaN(val);

const isPlainObject = (value: any): value is object =>
  Object.prototype.toString.call(value) === '[object Object]';

const filterNaNInternal = (value: any) => {
  if (value === null) {
    return value;
  }

  let updatedObj: any;
  if (typeof value === 'object') {
    if (Array.isArray(value)) {
      updatedObj = [];

      value.forEach((element) => {
        if (!isValueNaN(element)) {
          updatedObj.push(filterNaNInternal(element));
        }
      });
    } else if (isPlainObject(value)) {
      updatedObj = {};
      Object.entries(value).forEach(([key, val]) => {
        if (!isValueNaN(val)) {
          updatedObj[key] = filterNaNInternal(val);
        }
      });
    } else {
      updatedObj = value;
    }
  }
  // We're not handling for 'number' type here
  // because the assumption is that the input of the first invocation
  // of this function is an object.
  // For nested objects, we're already filtering the NaN values in the 'if' block.
  else {
    updatedObj = value;
  }

  return updatedObj;
};

const filterNaN = (value: Record<string, unknown> | null) => {
  try {
    return filterNaNInternal(value);
  } catch (err) {
    logError('An error occurred while filtering NaN values: ' + err);
    return value;
  }
};

export { filterNaN };
