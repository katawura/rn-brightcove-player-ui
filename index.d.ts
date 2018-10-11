import * as React from 'react';
import { ViewStyle } from 'react-native';

export type BrightcovePlayerUIProps = {
  /**
   * Proxy method that is used to close the video overlay. 
   * Will also explicitly stop the video playing and set the volume to 0.
   * @returns void  
   */
  close: () => void;
  policyKey: string;
  accountId: string;
  referenceId?: string;
  videoId?: string;
  autoPlay?: boolean;
  play?: boolean;
  volume?: number;
  onReady?: () => void;
  onPlay?: () => void;
  onPause?: () => void;
  onEnd?: () => void;
  onProgress?: ({ currentTime: number }) => void;
  onChangeDuration?: ({ duration: number }) => void;
  onUpdateBufferProgress?: ({ bufferProgress: number }) => void;
  share?: boolean;
}


export type BrightcovePlayerProps = {
  policyKey: string;
  accountId: string;
  referenceId?: string;
  videoId?: string;
  autoPlay?: boolean;
  play?: boolean;
  fullscreen?: boolean;
  disableDefaultControl?: boolean;
  volume?: number;
  onReady?: () => void;
  onPlay?: () => void;
  onPause?: () => void;
  onEnd?: () => void;
  onProgress?: ({ currentTime: number }) => void;
  onChangeDuration?: ({ duration: number }) => void;
  onUpdateBufferProgress?: ({ bufferProgress: number }) => void;
  onEnterFullscreen?: () => void;
  onExitFullscreen?: () => void;
  style?: ViewStyle;
};

export class BrightcovePlayer extends React.Component<BrightcovePlayerProps, {}> {}

export class BrightcovePlayerUI extends React.Component<BrightcovePlayerUIProps, {}> {
  seekTo(position: number): {};
}
