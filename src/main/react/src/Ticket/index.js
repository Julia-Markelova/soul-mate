import './Ticket.css';
import Menu from "../Menu";
import { useEffect, useState } from "react";
import { CircularProgress } from "@material-ui/core";
import TicketContent from "./TicketContent";


function Ticket() {
    const soulId = "49e883d7-1f63-4cf8-9cdb-f62615dd71fc";

    const [data, setData] = useState();

    const [isLoading, setIsLoading ] = useState(true);

    const [progress, setProgress] = useState(0);

    useEffect(() => {
        let isCancelled = false;

        const loadSouls = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/souls/${soulId}/life-tickets`);
                !isCancelled && response.json().then(x => !!x && setData(x));
            }
            catch (error) {
                !isCancelled && console.log(error.toString());
            }
        };

        const timer = setInterval(() => {
            setProgress((prevProgress) => (prevProgress >= 100 ? 0 : prevProgress + 10));
        }, 800);

        const timer2 = setInterval(async () => {
            if (!data) await loadSouls();
        }, 2000);

        return () => {
            clearInterval(timer);
            clearInterval(timer2);
            isCancelled = true;
        };
    }, [data]);

    return (
        <>
            <Menu />
            <div className="Ticket">
                {
                    isLoading && !data && (
                        <div >
                            <div>
                                <h1>Поиск билета в жизнь</h1>
                            </div>
                            <CircularProgress className="Ticket-spinner" variant="determinate" value={progress} />
                        </div>

                    )
                }
                {data && <TicketContent />}
            </div>
        </>
    );
}

export default Ticket;
