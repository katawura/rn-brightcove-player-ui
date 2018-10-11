# React Native Brightcove Player UI

A React Native implementation of Brightcove Player SDK and custom overlay.

<img src="https://user-images.githubusercontent.com/443965/40413410-b9963158-5eb0-11e8-924f-9f61df58fa04.jpg" width="500">

## Installation

1. In packages.json manually add to dependencies
```console
"rn-brightcove-player-ui": "joipolloi/rn-brightcove-player-ui#v0.1.6"
```

2. 
```console
react-native link rn-brightcove-player
```

### iOS

- Make `Podfile` like below and `pod install`

```rb
source 'https://github.com/brightcove/BrightcoveSpecs.git'

platform :ios, '9.0'
use_frameworks!

target 'Your-Project-Name' do
    pod 'Brightcove-Player-Core'
end
```

### Android

- Add following lines in `android/build.gradle`

```gradle
allprojects {
    repositories {
        maven {
            url 'http://repo.brightcove.com/releases'
        }
    }
}
```

## Usage

### BrightcovePlayerUI

```jsx
import { BrightcovePlayerUI } from 'rn-brightcove-player-ui';

export default class App extends Component {
  render() {
    return (
        <BrightcovePlayerUI
          autoplay={true}
          accountId="3636334163001"
          videoId="3666678807001"
          policyKey="BCpkADawqM1W-vUOMe6RSA3pA6Vw-VWUNn5rL0lzQabvrI63-VjS93gVUugDlmBpHIxP16X8TSe5LSKM415UHeMBmxl7pqcwVY_AZ4yKFwIpZPvXE34TpXEYYcmulxJQAOvHbv2dpfq-S_cm"
        />
    );
  }
}
```


### BrightcovePlayer (original component)
The original component can be accessed by importing from the ```rn-brightcove-player-ui``` namespace.
```jsx
import { BrightcovePlayer } from 'rn-brightcove-player-ui';

export default class App extends Component {
  render() {
    return (
      <View style={styles.container}>
        <BrightcovePlayer
          style={styles.player}
          accountId="3636334163001"
          videoId="3666678807001"
          policyKey="BCpkADawqM1W-vUOMe6RSA3pA6Vw-VWUNn5rL0lzQabvrI63-VjS93gVUugDlmBpHIxP16X8TSe5LSKM415UHeMBmxl7pqcwVY_AZ4yKFwIpZPvXE34TpXEYYcmulxJQAOvHbv2dpfq-S_cm"
        />
      </View>
    );
  }
}
```

- Specifying `accountId`, `policyKey`, and `videoId` or `referenceId` will load the video.
- Size of the player must be non-zero.
- It may not work on Android simulator, in that case please run on device.
- For a more detailed example, please see [https://github.com/manse/react-native-brightcove-player/blob/master/example/App.js](https://github.com/manse/react-native-brightcove-player/blob/master/example/App.js).

| Prop                   | Type     | Description                                                                     | Event Object                 | BrightcovePlayer | BrightcovePlayerUI |
| ---------------------- | -------- | ------------------------------------------------------------------------------- | ---------------------------- | ---------------- | ------------------ |
| accountId              | string   | Brightcove AccountId                                                            |                              | yes              | yes                |
| policyKey              | string   | Brightcove PolicyKey                                                            |                              | yes              | yes                |
| videoId                | string   | Brightcove VideoId. \*Either videoId or referenceId is required                 |                              | yes              | yes                |
| referenceId            | string   | Brightcove ReferenceId. \*Either videoId or referenceId is required             |                              | yes              | yes                |
| autoPlay               | boolean  | Whether to play automatically when video loaded                                 |                              | yes              | yes                |
| play                   | boolean  | Control playback and pause                                                      |                              | yes              | yes                |
| fullscreen             | boolean  | Control full screen state                                                       |                              | yes              | no                 |
| volume                 | number   | Set audio volume (0.0 - 1.0)                                                    |                              | yes              | yes                |
| disableDefaultControl  | boolean  | Disable default player control.                                                 |                              | yes              | no                 |
| onReady                | Function | Indicates the video can be played back                                          |                              | yes              | yes                |
| onPlay                 | Function | Indicates the video playback starts                                             |                              | yes              | yes                |
| onPause                | Function | Indicates the video is paused                                                   |                              | yes              | yes                |
| onEnd                  | Function | Indicates the video is played to the end                                        |                              | yes              | yes                |
| onProgress             | Function | Indicates the playback head of the video advances.                              | `{ currentTime: number }`    | yes              | yes                |
| onChangeDuration       | Function | Indicates the video length is changed                                           | `{ duration: number }`       | yes              | yes                |
| onUpdateBufferProgress | Function | Indicates the video loading buffer is updated                                   | `{ bufferProgress: number }` | yes              | yes                |
| onEnterFullscreen      | Function | Indicates the player enters full screen                                         |                              | yes              | no                 |
| onExitFullscreen       | Function | Indicates the player exit full screen                                           |                              | yes              | no                 |

| Method                                | Description                       | BrightcovePlayer | BrightcovePlayerUI |
| ------------------------------------- | --------------------------------- | ---------------- | ------------------ |
| seekTo(timeInSeconds: number) => void | Change playhead to arbitrary time |        yes       |         no         |
 
