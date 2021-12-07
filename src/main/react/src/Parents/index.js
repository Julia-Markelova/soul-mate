import * as React from "react";
import { useEffect, useState } from "react";
import Button from "@material-ui/core/Button";
import { useHistory } from "react-router";
import { useSelector } from 'react-redux';
import { Label } from "reactstrap";

function Parents() {
    const history = useHistory();

    const [data, setData] = useState([]);
    const token = useSelector(x => x.user.token);
    const role = useSelector(x => x.user.role);
    const [subscriptions, setSubscriptions] = useState([]);

    useEffect(() => {
        if (role !== "RELATIVE" || !token) {
            history.push('/');
        }
    }, [role, token, history]);

    useEffect(() => {
        if (!token) return;
        let isCancelled = false;
        const loadSouls = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/relatives/get-subscriptions`, {
                    headers: { 'soul-token': token }
                });
                !isCancelled && response.json().then(x => setSubscriptions(x));
            }
            catch (error) {
                !isCancelled && console.log(error.toString());
            }
        };

        loadSouls()

        return () => {
            isCancelled = true;
        };
    }, [token]);

    useEffect(() => {
        if (!token) return;
        let isCancelled = false;
        const loadSouls = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/relatives/get-new-messages`, {
                    headers: { 'soul-token': token }
                });
                !isCancelled && response.json().then(x => {
                    !!x?.length && setData(prev => [...prev, ...x])
                });
            }
            catch (error) {
                !isCancelled && console.log(error.toString());
            }
        };

        const timer2 = setInterval(async () => {
            if (subscriptions.some(x => x.subscribed)) await loadSouls();
        }, 2000);

        return () => {
            clearInterval(timer2);
            isCancelled = true;
        };
    }, [data, subscriptions, token]);

    const handleSubscribe = async (id, value) => {
        const response = await fetch(`http://localhost:8080/api/relatives/toggle-subscriptions?subscriptionId=${id}&enable=${value}`, {
            headers: { 'soul-token': token }
        });
        if (response.ok) {
            response.json().then(x => setSubscriptions(y => [...y.filter(z => z.id !== x.id), x]))
        }
    }

    return (
        <>
            {
                [...subscriptions].sort((a, b) => a.id.localeCompare(b.id)).map((x, i) => <div key={i} style={{ width: 'fit-content', margin: '10px auto' }}>
                    <Label>ID души: {x.soulId}</Label>
                    <Button onClick={e => handleSubscribe(x.id, !x.subscribed)}
                        style={{
                            margin: 'auto 10px',
                            // display: 'block',
                            backgroundColor: '#7b9cea',
                            color: 'white',
                            width: '200px',
                            height: '50px',
                            fontSize: '20px',
                            // marginTop: '33px'
                        }}>
                        {x.subscribed ? "Отписаться" : "Подписаться"}
                    </Button>
                </div>)
            }

            <div className="Ticket" style={{ textAlign: 'center' }}>
                {
                    !data.length && (
                        <div >
                            <div>
                                <h1>Новых уведомлений нет</h1>
                            </div>
                        </div>

                    )
                }
                {!!data?.length &&
                    <div className="TicketContent">
                        <h2 className="TicketContent-title">Уведомление</h2>
                        <div className="TicketContent-content">
                            <div className="TicketContent-info" style={{ whiteSpace: 'pre-wrap' }}>
                                {data.map(x => x.message).join('\n')}
                            </div>
                        </div>
                        <Button
                            style={{ top: '100px' }}
                            onClick={x => setData([])}
                            className="TicketContent-button">OK</Button>

                    </div>}
            </div>
        </>
    );
};

export default Parents;