import * as React from "react"
import { useDispatch, useSelector } from "react-redux";
import { useEffect } from "react";
import { receiveAutoMode } from "../Store/user-types";
import { useHistory } from "react-router";
import { Label } from "reactstrap";
import { Switch } from "@material-ui/core";

const AdminSettings = () => {
    const history = useHistory();
    const dispatch = useDispatch();

    const token = useSelector(x => x.user.token);
    const role = useSelector(x => x.user.role);

    const modes = useSelector(x => x.user.autoModes);
    console.log(modes)
    useEffect(() => {
        if (role !== "ADMIN" || !token) {
            history.push('/');
        }
    }, [role, token, history]);

    const handleMode = async (id, enabled) => {
        console.log(id, enabled)
        const response = await fetch(`http://localhost:8080/admin/changeMode/${id}?isManualMode=${enabled ? "true" : "false"}`, {
            headers: { 'soul-token': token }
        });

        if (response.ok) {
            dispatch(receiveAutoMode({ ...modes.find(y => y.id === id), isManualMode: enabled }))
        }
    }

    return <div>
        <br />
        <br/>
        <br />{
        [...modes].sort((a, b) => a.id.localeCompare(b.id)).map((x, i) => <div key={i} style={{ width: 'fit-content', margin: 'auto' }}>
            <Label>{
                x.type === "LIFE_TICKET_MODE" ? "Авто-выдача билетов в жизнь" : "Автоматический поиск искры жизни"
            }</Label>
            <Switch checked={!x.isManualMode} onChange={e => handleMode(x.id, !e.target.checked)} id={`switcher_${i}`} />
        </div>)
    }
    </div>
}

export default AdminSettings;