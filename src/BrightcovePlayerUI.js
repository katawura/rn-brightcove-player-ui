import React, { Component } from 'react';
import {
  StyleSheet,
  View,
  Dimensions,
  Image,
  StatusBar,
  Share,
  Slider,
  TouchableOpacity
} from 'react-native';
import { BrightcovePlayer } from 'rn-brightcove-player-ui';

export default class App extends Component {
  state = {
    playing: false,
    currentTime: 0,
    duration: 0,
    bufferProgress: 0,
    volume: 1,
    showOverlay: false
  };

  componentDidMount() {
    //this.toggleOverlay();
  }

  togglePlaying = ():void => {
      this.setState({playing: !this.state.playing});
  }

  toggleOverlay = (): void => {
    if (this.state.showOverlay) {
      this.setState({showOverlay: false});
    } else {
      this.setState({showOverlay: true});
      /*setTimeout(()=> {
        this.setState({showOverlay: false});
      }, 5000);*/
    }
  }

  close = ():void => {
    this.setState({playing: false, volume: 0})
    this.props.close();
  }

  /**
   * reject {action: "dismissedAction"}
   * resolve {action: "sharedAction", activityType: "com.apple.UIKit.activity.CopyToPasteboard"}
   */
  share = async(): Promise => {
    Share.share({ message: 'something'}).then((result: any) => {
      return new Promise(function(resolve, reject) {
        resolve(result);
      });
    }).catch((error:any)=> {
      return new Promise(function(resolve, reject) {
        reject(error);
      });
    });
  }

  render() {
    const toggleIcon:any = (this.state.playing) ? require('./assets/icon-pause.png') : require('./assets/icon-play.png'); 
    const shareIcon:JSX.Element = (this.props.share) ? 
    <TouchableOpacity style={styles.button} onPress={this.share}>
      <Image source={require('./assets/icon-share.png')} style={{width: 21, height: 21}} />
    </TouchableOpacity> :  <View />;

    return (
      <View style={ styles.container}>
        <StatusBar hidden={true} />
        {this.state.showOverlay ?
        <TouchableOpacity style={styles.controls} onPress={this.toggleOverlay}>
          <View style={styles.top}>
            <TouchableOpacity style={styles.button} onPress={this.close}>
              <Image source={require('./assets/icon-close.png')} style={{width: 21, height: 21}} />
            </TouchableOpacity>
          </View>

          <View style={styles.bottomControlls}>
            <View style={styles.sliderContainer}>
              <Slider minimumTrackTintColor="#fff" maximumTrackTintColor="#000" />
            </View>
            <View style={styles.bottom}>
              <View style={styles.bottomLeft}>
                <TouchableOpacity style={styles.button} onPress={this.togglePlaying}>
                  <Image source={toggleIcon} style={{width: 14, height: 21}} />
                </TouchableOpacity>
              </View>
              <View style={styles.bottomRight}>{shareIcon}</View>
            </View>
          </View>
        </TouchableOpacity> 
        : <TouchableOpacity style={ styles.containerHidden} onPress={this.toggleOverlay} /> }
      
        <BrightcovePlayer
          ref={ref => (this.player = ref)}
          style={styles.player}
          play={this.state.playing}
          autoPlay={this.props.autoPlay}
          fullscreen={false}
          disableDefaultControl={true}
          volume={this.state.volume}
          accountId={this.props.accountId}
          videoId={this.props.videoId}
          policyKey={this.props.policyKey}
          onReady={() => console.log('onReady')}
          onPlay={() => {
            this.setState({ playing: true });
          }}
          onPause={() => {
            this.setState({ playing: false });
          }}
          onEnd={() => console.log('onEnd')}
          onProgress={({ currentTime }) => {
            this.setState({ currentTime });
          }}
          onUpdateBufferProgress={({ bufferProgress }) => {
            this.setState({ bufferProgress });
          }}
          onChangeDuration={({ duration }) => {
            this.setState({ duration });
          }}
        />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'transparent',
    alignItems: 'center',
    justifyContent: 'center'
  },
  containerHidden: {
    position: 'absolute',
    top: 0,
    bottom: 0,
    right: 0,
    left: 0,
    zIndex: 1000
  },
  player: {
    width: Dimensions.get('window').height,
    height: Dimensions.get('window').width,
    transform: [{ rotate: '90deg'}],
  },
  controls: {
    position: 'absolute',
    width: Dimensions.get('window').height,
    height: Dimensions.get('window').width,
    flex: 1,
    justifyContent: 'space-between',
    backgroundColor: 'rgba(0,0,0,1)',
    zIndex: 100,
    transform: [{ rotate: '90deg'}]
  },
  top: {
    backgroundColor: 'rgba(0,0,0,0.0)',
    flexDirection: 'row',
    alignItems: 'flex-start',
    justifyContent: 'flex-start',
    paddingLeft: 1,
    paddingTop: 1
  },
  bottomControlls: {
  },
  sliderContainer: {
    flex: 1,
    marginBottom: 40
  },
  bottom: {
    backgroundColor: 'rgba(0,0,0,0.8)',
    alignItems: 'flex-start',
    justifyContent: 'center',
    flexDirection: 'row'
  },
  bottomLeft: {
    flex: 0.5,
    justifyContent:'center',
    alignItems: 'flex-start',
    padding:10 
  },
  bottomRight: {
    flex: 0.5,
    justifyContent:'center',
    alignItems: 'flex-end',
    padding:10 
  },
  button: {
    width: 44,
    height: 44,
    backgroundColor:'#000',
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'black'
  }

});