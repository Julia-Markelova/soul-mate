
import * as React from "react"
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router";
import { Button } from "reactstrap";
import { useEffect } from "react";
import { receiveHelpRequest, receiveHelpRequests } from "../Store/user-types";

const RequestHelp = () => {
    const dispatch = useDispatch();
    const history = useHistory();


    const token = useSelector(x => x.user.token);
    const role = useSelector(x => x.user.role);
    const soulStatus = useSelector(x => x.user.soulStatus);

    const thisSoulRequests = useSelector(x => x.user.helpRequests);

    useEffect(() => {
        if (!token) return;

        const load = async () => {
            const response = await fetch("http://localhost:8080/api/souls/my-requests/astral", {
                headers: { 'soul-token': token }
            });
            response.json().then(x => {console.log(x); dispatch(receiveHelpRequests(x))});
        }

        load();
    }, [dispatch, token])

    //get all soul requests, block creating new 
    useEffect(() => {
        if (role !== "SOUL" || !token || soulStatus !== "LOST") {
            history.push('/');
        }
    }, [role, token, soulStatus, history]);

    const handleClick = async () => {
        const response = await fetch(`http://localhost:8080/api/souls/create-help-request/astral`, {
            method: "POST",
            headers: {
                "soul-token": token
            }
        });
        if (response.ok) {
            response.json().then(x => dispatch(receiveHelpRequest(x)));
        }
    }

    const blockingRequest = React.useMemo(() => thisSoulRequests.find(x => x.status === "NEW" || x.status === "ACCEPTED"), [thisSoulRequests])

    return <div style={{ margin: '30px auto', width: 'fit-content' }}>
        {!blockingRequest ? <Button onClick={handleClick}>
            Спастись
        </Button>
            :
            <div style={{ width: 'fit-content' }}>{`Заявка создана. Текущий статус: ${blockingRequest.status}`}</div>
        }
    </div>
}

export default RequestHelp;