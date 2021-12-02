import './Menu.css';
import { Link } from "react-router-dom";
import { Button, Input } from "reactstrap"
import * as React from "react";
import { useState  } from "react";
import { useDispatch, useSelector } from 'react-redux';
import { receiveRole, receiveToken, receiveUserId } from '../Store/user-types';

function Menu() {
    const dispatch = useDispatch();

    const [name, setName] = useState("");
    const [password, setPassword] = useState("");

    const role = useSelector(s => s.user.role);

    const handleSubmit = async () => {
        const response = await fetch(`http://localhost:8080/login?login=${name}&password=${password}`, { method: "POST" });
        response.json().then(x => {
            localStorage.setItem('token', x.token)
            dispatch(receiveToken( x.token));
            dispatch(receiveRole(x.role));
            dispatch(receiveUserId(x.id));
        })
    }

    const handleLogout = () => {
        localStorage.removeItem('token')
        dispatch(receiveToken(''));
        dispatch(receiveRole(''));
    }

    return (
        <div className="Menu">
            {!!role
                ? <>

                    {role === "ADMIN" && <h3>
                        <Link to="/souls">Список душ</Link>
                    </h3>}
                    <h3>
                        <Link to="/parents">Для родственников</Link>
                    </h3>
                    {role === "SOUL" &&
                        <>
                            <h3>
                                <Link to="/ticket">Билет в жизнь</Link>
                            </h3>
                            <h3>
                                <Link to="/soul-help">Спасение</Link>
                            </h3>
                        </>
                    }
                    {
                        role === "GOD" &&
                        <>
                        <h3>
                            <Link to="/god-help">Заявки на спасение</Link>
                        </h3>
                        </>
                    }
                    <Button onClick={handleLogout} >Log out</Button>
                </>
                : <>
                    <h3>
                        <Link to="/">На главную</Link>
                    </h3>
                    <Input type="text" value={name} onChange={e => setName(e.target.value)} />
                    <Input type="text" value={password} onChange={e => setPassword(e.target.value)} />
                    <Button onClick={handleSubmit}>Log in</Button>
                </>
            }
        </div>
    );
}

export default Menu;
