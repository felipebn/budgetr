import React, { Component } from 'react';
import gql from 'graphql-tag';
import { Query } from 'react-apollo';
import {formatMoney} from '../utils'
import Loader from './loader/Loader'

class PeriodBudgetPanel extends Component{
  
    render(){
      const items = this.props.budget.expenses;
      const startDate = this.props.budget.start;
      const endDate = this.props.budget.end;
      const total = this.props.budget.total;
      
      return (
        <div className='box'>
          <div className='level'>
            <div className="level-left ">
              <div className="level-item">
                <div className='title is-size-5'>
                  Budget from {startDate} to {endDate}  
                </div>
              </div>
            </div>
            <div className="level-right">
              <div className="level-item">
                <div className='title is-size-4 has-text-link'>
                  {formatMoney(total)}  
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
              {items.map(this.renderRow.bind(this))}
            </tbody>
          </table>
        </div>
      );
    }
    
    renderRow(item, index){
      return (
        <tr key={"budget-entry" + index}>
          <td>{item.description}</td>
          <td>{item.plannedDate}</td>
          <td>{formatMoney(item.amount)}</td>
        </tr>
      )
    }
}

const GET_BUDGET = gql`
{
  currentBudget{
    start,
    end,
    total,
    expenses {
      description,
      plannedDate,
      amount
    }
  }
}
`;

export default () => (
  <Query query={GET_BUDGET}>
    {({ loading, data }) => {
      if(loading){
        return <div className='box has-text-centered'><Loader/></div>
      }else{
        return <PeriodBudgetPanel budget={data.currentBudget}/>
      }
    }}
  </Query>
);
