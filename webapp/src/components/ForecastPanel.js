import React, { Component } from 'react';
import HorizontalTimeline from './HorizontalTimeline';

class ForecastPanel extends Component{
    render(){
      return (
        <div className='box'>
          <div className="title is-size-5">
            Forecast from {this.props.startDate} to {this.props.endDate}
          </div>
          <HorizontalTimeline items={this.props.forecastItems}/>
        </div>
      )
    }
}

export default ForecastPanel;