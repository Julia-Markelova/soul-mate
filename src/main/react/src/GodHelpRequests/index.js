
import * as React from "react"
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router";
import { Button } from "reactstrap";
import { DataGrid } from "@material-ui/data-grid";
import { receiveHelpRequests, receiveHelpRequest, removeHelpRequest } from "../Store/user-types";

export const getRequestDescription = (value) => {
    switch (value.toLowerCase()) {
        case "new":
            return "Новая"
        case "accepted":
            return "Принята"
        case "finished":
            return "Завершена"
    }
}

export const HelpRequestTable = (props) => {
    const { onAcceptUrl, onFinishUrl, onRejectUrl } = props;

    const dispatch = useDispatch();

    const token = useSelector(x => x.user.token);
    const helpRequests = useSelector(x => x.user.helpRequests ?? []);

    const handleRequest = async (id, status) => {
        let response;
        switch (status) {
            case "NEW":
                response = await fetch(onAcceptUrl + id, {
                    method: "PUT",
                    headers: { 'soul-token': token }
                });
                break;
            case "ACCEPTED":
                response = await fetch(onFinishUrl + id, {
                    method: "PUT",
                    headers: { 'soul-token': token }
                });
                break;
            default:
                console.log(status);
                break;
        }
        response.json().then(x => dispatch(receiveHelpRequest(x)));
    }
    const handleReject = async (id, status) => {
        let response;
        switch (status) {
            case "NEW":
                response = await fetch(onRejectUrl + id, {
                    method: "PUT",
                    headers: { 'soul-token': token }
                });
                break;
            default:
                console.log(status);
                break;
        }
        if (response.ok) dispatch(removeHelpRequest(id));
    }

    const columns = [
        {
            field: 'createdBy',
            headerName: 'Спасаемый',
            width: 300
        }, {
            field: 'status',
            headerName: 'Статус',
            width: 300,
            renderCell: params => getRequestDescription(params.row.status)
        },
        {
            field: 'id',
            headerName: 'Действие',
            width: 300,
            renderCell: params => {
                const onClick = async e => {
                    e.stopPropagation();
                    await handleRequest(params.row.id, params.row.status);
                }
                switch (params.row.status) {
                    case "NEW":
                        return <>
                            <Button onClick={onClick} >Откликнуться</Button>
                            <Button onClick={e => { e.stopPropagation(); handleReject(params.row.id, params.row.status) }}>Отклонить</Button>
                        </>
                    case "ACCEPTED":
                        return <Button onClick={onClick}>Завершить</Button>
                    default:
                        return <>Завершено</>
                }
            }
        }
    ]

    return <div style={{ height: 'auto', minHeight: '600px', marginTop: '30px' }}>
        <DataGrid rows={helpRequests} columns={columns} pageSize={10} autoHeight />
    </div>
}

const GodHelpRequests = () => {
    const dispatch = useDispatch();
    const history = useHistory();

    const token = useSelector(x => x.user.token);
    const role = useSelector(x => x.user.role);

    useEffect(() => {
        if (role !== "GOD" || !token) {
            history.push('/');
        }
    }, [role, token, history]);

    useEffect(() => {
        if (!token) return;
        const load = async () => {
            const data = await fetch('http://localhost:8080/api/gods/open-requests', {
                headers: { 'soul-token': token }
            });

            data.json().then(x => dispatch(receiveHelpRequests(x)));
        }
        load();
    }, [token, dispatch]);

    return <HelpRequestTable
        onAcceptUrl="http://localhost:8080/api/gods/accept-request/"
        onFinishUrl="http://localhost:8080/api/gods/finish-request/"
        onRejectUrl="http://localhost:8080/api/gods/reject-request/"
    />
}

export default GodHelpRequests;