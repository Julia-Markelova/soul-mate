import * as React from "react";
import { useEffect, useState } from "react";
import Button from "@material-ui/core/Button";
import { useHistory } from "react-router";
import { useSelector } from 'react-redux';

function Parents() {
    const history = useHistory();

    const [data, setData] = useState([]);
    const [isSubscribed, setIsSubscribed] = useState(false);
    const token = useSelector(x => x.user.token);
    const role = useSelector(x => x.user.role);

    useEffect(() => {
        if (role !== "RELATIVE" || !token) {
            history.push('/');
        }
    }, [role, token, history]);

    useEffect(() => {
        if (!token) return;
        let isCancelled = false;
        console.log(1);
        const loadSouls = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/relatives/get-subscriptions`, {
                    headers: { 'soul-token': token }
                });
                !isCancelled && response.json().then(x =>  console.log(x));
            }
            catch (error) {
                !isCancelled && console.log(error.toString());
            }
        };

        loadSouls()

        return () => {
            isCancelled = true;
        };
    }, [isSubscribed, token]);

    useEffect(() => {
        if (!token) return;
        let isCancelled = false;
        console.log(2);
        const loadSouls = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/relatives/get-new-messages`, {
                    headers: { 'soul-token': token }
                });
                !isCancelled && response.json().then(x => { console.log(x)
                    //!!x?.length && setData(prev => [...prev, ...x])
                });
            }
            catch (error) {
                !isCancelled && console.log(error.toString());
            }
        };

        const timer2 = setInterval(async () => {
            if (isSubscribed) await loadSouls();
        }, 2000);

        return () => {
            clearInterval(timer2);
            isCancelled = true;
        };
    }, [data, isSubscribed, token]);

    const handleSubscribe = async (value) => {
        // await fetch(`http://localhost:8080/api/relatives/${userId}/subscriptions?enable=${!isSubscribed}`);
        setIsSubscribed(value)
    }

    return (
        <>
            <Button onClick={x => handleSubscribe(x.target.checked)}
                style={{
                    margin: 'auto',
                    display: 'block',
                    backgroundColor: '#7b9cea',
                    color: 'white',
                    width: '200px',
                    height: '50px',
                    fontSize: '20px',
                    marginTop: '33px'
                }}>
                {isSubscribed ? "Отписаться" : "Подписаться"}
            </Button>

            <div className="Ticket">
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