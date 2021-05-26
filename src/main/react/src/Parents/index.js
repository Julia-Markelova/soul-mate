import * as React from "react";
import {useEffect, useState} from "react";
import Button from "@material-ui/core/Button";

function Parents() {
    const userId = "b3b04914-0434-4f6e-8d86-d3021220a62a";

    const [data, setData] = useState([]);
    const [isSubscribed, setIsSubscribed] = useState(false);

    useEffect(() => {
        let isCancelled = false;

        const loadSouls = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/relatives/${userId}/subscriptions-status`);
                !isCancelled && response.json().then(x => setIsSubscribed(x));
            }
            catch (error) {
                !isCancelled && console.log(error.toString());
            }
        };

        loadSouls()

        return () => {
            isCancelled = true;
        };
    }, [isSubscribed]);

    useEffect(() => {
        let isCancelled = false;

        const loadSouls = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/notifications/${userId}`);
                !isCancelled && response.json().then(x => {
                    !!x?.length && setData(prev => [...prev, ...x])
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
    }, [data, isSubscribed]);

    const handleSubscribe = async (value) => {
        await fetch(`http://localhost:8080/api/relatives/${userId}/subscriptions?enable=${!isSubscribed}`);
        setIsSubscribed(value)
    }

    return (
        <>
            <Button onClick={x => handleSubscribe(x.target.checked)}
                style={{margin: 'auto',
                    display: 'block',
                    backgroundColor: '#7b9cea',
                    color: 'white',
                    width: '200px',
                    height: '50px',
                    fontSize: '20px',
                    marginTop: '33px'}}>
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
                        <div className="TicketContent-info" style={{whiteSpace: 'pre-wrap'}}>
                            {data.map(x => x.message).join('\n')}
                        </div>
                    </div>
                    <Button
                        style={{top:'100px'}}
                        onClick={x => setData([])}
                        className="TicketContent-button">OK</Button>

                </div>}
            </div>
        </>
    );
};

export default Parents;