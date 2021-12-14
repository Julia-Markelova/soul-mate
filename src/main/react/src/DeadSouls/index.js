
import * as React from "react"
import { useDispatch, useSelector } from "react-redux";
import { useHistory } from "react-router";
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from "reactstrap";
import { useEffect, useState } from "react";
import { receiveHelpRequest, receiveHelpRequests, receiveSoulStatus } from "../Store/user-types";
import { getRequestDescription } from "../GodHelpRequests";

const RequestHelp = () => {
    const dispatch = useDispatch();
    const history = useHistory();
    const [openModal, setOpenModal] = useState(false);

    const token = useSelector(x => x.user.token);
    const role = useSelector(x => x.user.role);
    const soulStatus = useSelector(x => x.user.soulStatus);

    const thisSoulRequests = useSelector(x => x.user.helpRequests ?? []);

    useEffect(() => {
        if (!token) return;

        const load = async () => {
            const response = await fetch("http://localhost:8080/api/souls/my-requests/astral", {
                headers: { 'soul-token': token }
            });
            response.json().then(x => dispatch(receiveHelpRequests(x)));
        }

        load();
    }, [dispatch, token]);

    const blockingRequest = React.useMemo(() => thisSoulRequests.find(x => x.status === "NEW" || x.status === "ACCEPTED"), [thisSoulRequests])
    useEffect(() => {
        if (!token) return;
        let isCancelled = false;
        const loadSouls = async () => {
            try {
                const response = await fetch("http://localhost:8080/api/souls/my-requests/astral", {
                    headers: { 'soul-token': token }
                });
                let request = null;
                !isCancelled && response.json().then(x => {
                    request = x.find(y => y.id === blockingRequest.id)
                    if (!!request && request.status !== blockingRequest.status) {
                        dispatch(receiveHelpRequest(request));
                        setOpenModal(true)
                        if (request.status === "FINISHED") {
                            dispatch(receiveSoulStatus("DEAD"));
                        }
                    }

                });
            }
            catch (error) {
                !isCancelled && console.log(error.toString());
            }
        };

        const timer2 = setInterval(async () => {
            await loadSouls();
        }, 10000);

        return () => {
            clearInterval(timer2);
            isCancelled = true;
        };
    }, [token, blockingRequest, dispatch]);

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


    return <div style={{ margin: '30px auto', width: 'fit-content' }}>
        {!blockingRequest ? <Button onClick={handleClick}>
            Спастись
        </Button>
            :
            <>
                <div style={{ width: 'fit-content' }}>{`Заявка создана. Текущий статус: ${getRequestDescription(blockingRequest.status)}`}</div>
                <Modal isOpen={openModal}>
                    <ModalHeader>
                        Новое уведомление
                    </ModalHeader>
                    <ModalBody>
                        <div style={{ width: 'fit-content' }}>{`Заявка создана. Текущий статус: ${getRequestDescription(blockingRequest.status)}`}</div>
                    </ModalBody>
                    <ModalFooter>
                        <Button onClick={e => setOpenModal(false)}>
                            OK
                        </Button>
                    </ModalFooter>
                </Modal>
            </>
        }

    </div>
}

export default RequestHelp;