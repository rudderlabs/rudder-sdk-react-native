import { logError } from './Logger';

export function filterNaN(value: Record<string, unknown> | null): Record<string, unknown> | null {
  if (value == null) return null;

  function convertNestedObjects(obj: Record<string, unknown>): Record<string, unknown> {
    const updatedObj: Record<string, unknown> = {};

    try {
      for (const [key, val] of Object.entries(obj)) {
        if (typeof val === 'number' && isNaN(val)) {
          // Drop NaN values, as it is not supported by the React Native bridge layer.
          continue;
        } else if (val === null) {
          updatedObj[key] = null;
        } else if (typeof val === 'object') {
          if (!Array.isArray(val)) {
            updatedObj[key] = convertNestedObjects(val as Record<string, unknown>);
          } else {
            updatedObj[key] = val.map((item) => {
              return convertNestedObjects(item as Record<string, unknown>);
            });
          }
        } else {
          updatedObj[key] = val;
        }
      }
    } catch (error) {
      logError('An error occurred while filtering NaN values: ' + error);
    }

    return updatedObj;
  }

  return convertNestedObjects(value);
}
