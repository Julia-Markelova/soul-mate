import './App.css';
import { Route, Switch, BrowserRouter } from 'react-router-dom';
import SoulsList from './SoulsList';
import Home from './Home';
import Ticket from "./Ticket";
import Parents from "./Parents";

function App() {
  return (
      <BrowserRouter>
          <Switch>
              <Route exact path='/' component={Home} />
              <Route path='/souls' component={SoulsList} />
              <Route path='/ticket' component={Ticket} />
              <Route path='/parents' component={Parents} />
          </Switch>
      </BrowserRouter>
  );
}

export default App;