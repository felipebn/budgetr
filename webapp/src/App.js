import React, { Component } from 'react';
import PeriodBudgetPanel from './components/PeriodBudgetPanel';
import ForecastPanel from './components/ForecastPanel';

class App extends Component{
  
  render(){
    var forecast = {
      start:'24/02/2019', 
      end:'06/04/2019',
      items:[
        this.createForecastItem('24/02 to 02/03', -4190, 4500),
        this.createForecastItem('03/03 to 09/03', -800, 3700),
        this.createForecastItem('10/03 to 16/03', -260, 3400),
        this.createForecastItem('17/03 to 23/03', -275, 3150),
        this.createForecastItem('24/03 to 30/03', -280, 4900),
        this.createForecastItem('31/03 to 06/04', -600, 4300),
      ]
    }
    
    return (<div className="container">
          <br/>
          <PeriodBudgetPanel/>
          <ForecastPanel
            startDate={forecast.start} 
            endDate={forecast.end}
            forecastItems={forecast.items}
            balanceEstimations={forecast.balanceEstimations}
            />
    </div>);
  }
  
  createBudgetItem(description, type, date, amount){
    return {description, type, date, amount}
  }
  
  createForecastItem(description, amount, balance){
    return {description, amount, balance}
  }
}

export default App;
