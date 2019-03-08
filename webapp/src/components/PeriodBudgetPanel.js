import React, { Component } from 'react';
import {formatMoney} from '../Utils'

class PeriodBudgetPanel extends Component{
  
    render(){
      var budgetItems = this.props.budgetItems;
      
      return (
        <div className='box'>
          <div className='level'>
            <div className="level-left ">
              <div class="level-item">
                <div className='title is-size-5'>
                  Budget from {this.props.startDate} to {this.props.endDate}  
                </div>
              </div>
            </div>
            <div className="level-right">
              <div className="level-item">
                <div className='title is-size-4 has-text-link'>
                  {formatMoney(this.props.total)}  
                </div>
              </div>
            </div>
          </div>
  
          <table className="table is-fullwidth">
            <thead>
              <tr>
                <th>Description</th>
                <th>Planned Date</th>
                <th>Amount</th>
              </tr>
            </thead>
            <tbody>
              {budgetItems.map(this.renderRow.bind(this))}
            </tbody>
          </table>
        </div>
      );
    }
    
    renderRow(item, index){
      return (
        <tr key={"budget-entry" + index}>
          <td>{item.description}</td>
          <td>{item.date}</td>
          <td>{formatMoney(item.amount)}</td>
        </tr>
      )
    }
}

export default PeriodBudgetPanel;