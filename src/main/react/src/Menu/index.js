import './Menu.css';
import { Link } from "react-router-dom";
import { Button, Input } from "reactstrap"
import * as React from "react";
import { useState } from "react";
import { useDispatch, useSelector } from 'react-redux';
import { receiveRole, receiveToken, receiveUserId, receiveSoulStatus, recieveIsMentor, receiveAutoModes, clearState } from '../Store/user-types';

function Menu() {
    const dispatch = useDispatch();

    const [name, setName] = useState("");
    const [password, setPassword] = useState("");

    const role = useSelector(s => s.user.role);
    const soulStatus = useSelector(s => s.user.soulStatus);
    const token = useSelector(s => s.user.token);

    const handleSubmit = async () => {
        const response = await fetch(`http://localhost:8080/login?login=${name}&password=${password}`, { method: "POST" });
        response.json().then(x => {
            localStorage.setItem('token', x.token)
            dispatch(receiveToken(x.token));
            dispatch(receiveRole(x.role));
            dispatch(receiveUserId(x.id));
        })
    }

    const canShowSaveAstral = React.useMemo(() => role === "SOUL" && soulStatus === "LOST", [role, soulStatus]);
    const canShowLifeTicket = React.useMemo(() => role === "MENTOR" || (role === "SOUL" && (soulStatus === "UNBORN" || soulStatus === "DEAD")), [role, soulStatus]);

    React.useEffect(() => {
        if (!!token) {
            const load = async () => {
                if (role === "SOUL") {
                    const response = await fetch(`http://localhost:8080/api/souls/profile`, {
                        headers: { 'soul-token': token }
                    });
                    if (response.ok) {
                        response.json().then(x => {
                            dispatch(receiveSoulStatus(x.status));
                            dispatch(recieveIsMentor(x.mentor));
                        });
                    }
                }

                if (role === "ADMIN" || role === "SOUL") {
                    const response = await fetch(`http://localhost:8080/admin/getAllModes`, {
                        headers: { 'soul-token': token }
                    });
                    if (response.ok) {
                        response.json().then(x => {
                            dispatch(receiveAutoModes(x));
                        });
                    }
                }
            }
            load();
        }
    }, [role, token, dispatch]);

    const handleLogout = () => {
        localStorage.removeItem('token')
        dispatch(clearState(undefined));
    }

    return (
        <div className="Menu">
            {!!role
                ? <>

                    {role === "ADMIN" &&
                        <> <h3>
                            <Link to="/souls">???????????? ??????</Link>
                        </h3><h3>
                                <Link to="/admin-settings">??????????????????</Link>
                            </h3>
                        </>}
                    {
                        role === "RELATIVE" && <>
                            <h3>
                                <Link to="/parents">?????? ??????????????????????????</Link>
                            </h3>
                        </>
                    }
                    {
                        role === "MENTOR" && <>
                            <h3>
                                <Link to="/ticket">???????????????????? ??????</Link>
                            </h3>
                        </>
                    }
                    {role === "SOUL" &&
                        <>
                            {canShowLifeTicket && <h3>
                                <Link to="/ticket">?????????? ?? ??????????</Link>
                            </h3>}
                            {canShowSaveAstral && <h3>
                                <Link to="/soul-help">????????????????</Link>
                            </h3>}
                        </>
                    }
                    {
                        role === "GOD" &&
                        <>
                            <h3>
                                <Link to="/god-help">???????????? ???? ????????????????</Link>
                            </h3>
                        </>
                    }
                    <Button onClick={handleLogout} >Log out</Button>
                </>
                : <>
                    <Link className="menu-link" to="/">???? ??????????????</Link>
                    <Input type="text" value={name} onChange={e => setName(e.target.value)} placeholder="??????????" className="menu-input" />
                    <Input type="text" value={password} onChange={e => setPassword(e.target.value)} placeholder="????????????" className="menu-input"/>
                    <Button onClick={handleSubmit}>Log in</Button>
                </>
            }
        </div>
    );
}

export default Menu;
