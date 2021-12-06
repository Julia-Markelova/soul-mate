import * as React from "react"
import { useDispatch, useSelector } from "react-redux";
import { useEffect } from "react";
import { receiveHelpRequest, receiveHelpRequests } from "../Store/user-types";
import { useHistory } from "react-router";
import { Label } from "reactstrap";
import { Switch } from "@material-ui/core";

const AdminSettings = () => {
    const history = useHistory();

    const token = useSelector(x => x.user.token);
    const role = useSelector(x => x.user.role);

    const [modes, setModes] = React.useState([]);

    useEffect(() => {
        if (role !== "ADMIN" || !token) {
            history.push('/');
        }
        else {
            const load = async () => {
                const response = await fetch("http://localhost:8080/admin/getAllModes", {
                    headers: { 'soul-token': token }
                });
                response.json().then(x => { setModes(x) });
            }

            load();
        }
    }, [role, token]);

    const handleMode = async (id, enabled) => {
        console.log(id, enabled)
        const response = await fetch(`http://localhost:8080/admin/changeMode/${id}?isManualMode=${enabled ? "true" : "false"}`, {
            headers: { 'soul-token': token }
        });

        if (response.ok) {
            setModes(x => [...x.filter(y => y.id !== id), { ...x.find(y => y.id === id), isManualMode: enabled }])
        }
    }

    return <>{
        [...modes.sort((a, b) => a.id.localeCompare(b.id))].map((x, i) => <div key={i}>
            <Label>{
                x.type === "LIFE_TICKET_MODE" ? "Авто-выдача билетов в жизнь" : "Автоматический поиск искры жизни"
            }</Label>
            <Switch checked={!x.isManualMode} onChange={e => handleMode(x.id, !e.target.checked)} />
        </div>)
    }
    </>
}

export default AdminSettings;