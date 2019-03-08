import React, { Component } from 'react';
import {formatMoney} from '../Utils'

class HorizontalTimeline extends Component{
    render(){
      return (
          <div>
            <ul className='custom-forecast-timeline'>
              {this.props.items.map(this.renderItem.bind(this))}
            </ul>
          </div>
      )
    }
      
    renderItem(item, index){    
      var bottomAdditionalClasses = item.balance > 0 ? 'positive' : 'negative'
      return (
        <li key={"ht-"+index}>
          <div className='top is-size-7'>
            {item.description}
          </div>
          <div className={'bottom is-size-7 ' + bottomAdditionalClasses}>
            {formatMoney(item.amount)}
          </div>
          <div className='is-size-7 has-text-link'>
            {formatMoney(item.balance)}
          </div>
        </li>
      )
    }
}
  
  export default HorizontalTimeline;
  