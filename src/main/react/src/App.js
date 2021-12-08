import './App.css';
import { Route, Switch, BrowserRouter } from 'react-router-dom';
import SoulsList from './SoulsList';
import Home from './Home';
import Ticket from "./Ticket";
import Parents from "./Parents";
import Menu from "./Menu";
import { useEffect } from 'react';
import RequestHelp from './DeadSouls';
import GodHelpRequests from './GodHelpRequests';
import { useDispatch } from 'react-redux';
import { receiveRole, receiveToken, receiveUserId, receiveRoleId } from './Store/user-types';
import AdminSettings from './AdminSettings';

function App() {
    const dispatch = useDispatch();

    const token = localStorage.getItem('token');

    useEffect(() => {
        if (token) {
            const loadUser = async () => {
                const response = await fetch('http://localhost:8080/getUser', {
                    headers: { 'soul-token': token }
                })
                
                response.json().then(x => {
                    dispatch(receiveToken(token))
                    !!x.role && dispatch(receiveRole(x.role))
                    !!x.id && dispatch(receiveUserId(x.id));
                    !!x.roleId && dispatch(receiveRoleId(x.roleId));
                })
            }
            loadUser();
        }
    }, [token, dispatch]);


    return (
        <BrowserRouter>
            <Menu />
            <Switch>
                <Route exact path='/' component={Home} />
                <Route path='/souls' component={SoulsList} />
                <Route path='/ticket' component={Ticket} />
                <Route path='/ticket' component={Ticket} />
                <Route path='/soul-help' component={RequestHelp} />
                <Route path='/god-help' component={GodHelpRequests} />
                <Route path='/admin-settings' component={AdminSettings} />
                <Route path='/parents' component={Parents} />
            </Switch>
        </BrowserRouter>
    );
}

export default App;
