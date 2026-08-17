export interface ISizeCalculationResult {
  width: number;
  height: number;
  type?: string;
}

export type Uint8ArrayInput = Uint8Array | Buffer;

export declare function imageSize(input: Uint8ArrayInput): ISizeCalculationResult;
export declare function disableTypes(types: string[]): void;
export declare const types: string[];
export default imageSize;
