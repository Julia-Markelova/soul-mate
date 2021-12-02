
import { RepeatOneSharp } from "@material-ui/icons";
import * as React from "react"
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router";
import { Button } from "reactstrap";
import { receiveHelpRequests } from "../Store/user-types";

const RequestHelp = () => {
    const dispatch = useDispatch();
    const history = useHistory();


    const token = useSelector(x => x.user.token);
    const role = useSelector(x => x.user.role);
    const id = useSelector(x => x.user.userId);
    const thisSoulRequests = useSelector(x => x.user.helpRequests.filter(x => x.createdBy === id));

    useEffect(() => {
        if (!token) return;

        const load = async () => {
            const response = await fetch("http://localhost:8080/api/souls/my-requests", {
                headers: { 'soul-token': token }
            });
            response.json().then(x => dispatch(receiveHelpRequests(x)));
        }

        load();
    }, [dispatch, token])

    //get all soul requests, block creating new 
    useEffect(() => {
        if (role !== "SOUL" || !token) {
            history.push('/');
        }
    }, [role, token]);

    const handleClick = async () => {
        const response = await fetch(`http://localhost:8080/api/souls/create-help-request`, {
            method: "POST",
            headers: {
                "soul-token": token
            }
        })
    }

    const canCreateRequest = React.useMemo(() => !thisSoulRequests.some(x => x.status === "NEW" || x.status === "ACCEPTED"), [thisSoulRequests])

    return <>
        {canCreateRequest && <Button onClick={handleClick}>
            Спастись
        </Button>}
    </>
}

export default RequestHelp;