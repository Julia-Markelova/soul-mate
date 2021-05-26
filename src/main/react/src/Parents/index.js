import * as React from "react";
import {useEffect, useState} from "react";
import Menu from "../Menu";
import {CircularProgress} from "@material-ui/core";
import TicketContent from "../Ticket/TicketContent";

function Parents() {
    const userId = "82441c9d-47d2-4d3d-8a05-414a34ebec8a";

    const [data, setData] = useState();

    const [isLoading, setIsLoading ] = useState(true);

    const [progress, setProgress] = useState(0);

    useEffect(() => {
        let isCancelled = false;

        const loadSouls = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/notifications/${userId}`);
                !isCancelled && response.json().then(x => {
                    !!x?.length && setData(x)
                });
            }
            catch (error) {
                !isCancelled && console.log(error.toString());
            }
        };

        const timer2 = setInterval(async () => {
            await loadSouls();
        }, 2000);

        return () => {
            clearInterval(timer2);
            isCancelled = true;
        };
    }, [data]);

    return (
        <>
            <Menu />
            <div className="Ticket">
                {
                    !data && (
                        <div >
                            <div>
                                <h1>Новых уведомлений нет</h1>
                            </div>
                        </div>

                    )
                }
                {!!data?.length && data.map((x, i) => <div key={i}>{x.message}</div>)}
            </div>
        </>
    );
}

export default Parents;