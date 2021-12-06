
import * as React from "react"
import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router";
import { Button } from "reactstrap";
import { DataGrid } from "@material-ui/data-grid";
import { receiveHelpRequests, receiveHelpRequest, removeHelpRequest } from "../Store/user-types";

const GodHelpRequests = () => {
    const dispatch = useDispatch();
    const history = useHistory();

    const token = useSelector(x => x.user.token);
    const role = useSelector(x => x.user.role);

    useEffect(() => {
        if (!token) return;
        const load = async () => {
            const data = await fetch('http://localhost:8080/api/gods/open-requests', {
                headers: { 'soul-token': token }
            });

            data.json().then(x => dispatch(receiveHelpRequests(x)));
        }
        load();
    }, [token]);

    const helpRequests = useSelector(x => x.user.helpRequests);

    useEffect(() => {
        if (role !== "GOD" || !token) {
            history.push('/');
        }
    }, [role, token]);

    const handleRequest = async (id, status) => {
        let response;
        switch (status) {
            case "NEW":
                response = await fetch("http://localhost:8080/api/gods/accept-request/" + id, {
                    method: "PUT",
                    headers: { 'soul-token': token }
                });
                break;
            case "ACCEPTED":
                response = await fetch("http://localhost:8080/api/gods/finish-request/" + id, {
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
                response = await fetch("http://localhost:8080/api/gods/reject-request/" + id, {
                    method: "PUT",
                    headers: { 'soul-token': token }
                });
                break;
            default:
                console.log(status);
                break;
        }
        if (response.ok()) dispatch(removeHelpRequest(id));
    }

    const columns = [
        {
            field: 'createdBy',
            headerName: 'Спасаемый',
            width: 300
        }, {
            field: 'status',
            headerName: 'Статус',
            width: 300
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
                            <Button onClick={e => { e.stopPropagation(); handleReject(params.row.id, params.row.status)} }>Отклонить</Button>
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
        <DataGrid rows={helpRequests} columns={columns} pageSize={10} autoHeight  />
    </div>
}

export default GodHelpRequests;