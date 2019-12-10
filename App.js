import React, {Component} from 'react';
import {View, Text, TouchableOpacity} from 'react-native';
import _ from 'lodash';

import ScrollView from './MyScrollView';
// CustomScrollView
// const ScrollView = requireNativeComponent('RNCustomScrollView');

export default class App extends Component {
  state = {
    data: _.range(100),
  };

  componentDidMount() {
    setTimeout(() => {
      this._list && this._list.autoScroll({duration: 200});
    }, 3000);
  }

  renderItem = ({item}) => {
    return (
      <TouchableOpacity>
        <View
          key={item}
          style={{
            height: 50,
            width: 70,
            alignItems: 'center',
            justifyContent: 'center',
            borderTopWidth: 1,
            borderBottomWidth: 1,
            borderLeftWidth: 1,
          }}>
          <Text>{item}</Text>
        </View>
      </TouchableOpacity>
    );
  };
  setRef = sef => (this._list = sef);

  render() {
    return (
      <ScrollView style={{paddingTop: 100}} horizontal ref={this.setRef}>
        {_.map(this.state.data, item => this.renderItem({item}))}
      </ScrollView>
    );
  }
}
