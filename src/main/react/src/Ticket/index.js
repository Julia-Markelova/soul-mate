import './Ticket.css';
import { useEffect, useState } from "react";
import { CircularProgress } from "@material-ui/core";
import TicketContent from "./TicketContent";


function Ticket() {
    const soulId = "b1a08ac2-fc48-4ec7-99cc-24d3c0f8e4fc";

    const [data, setData] = useState();

    const [progress, setProgress] = useState(0);

    useEffect(() => {
        let isCancelled = false;

        const loadSouls = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/souls/${soulId}/life-tickets`);
                !isCancelled && response.json().then(x => !!x && setData(x)).catch(x => console.log(x));
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
            <div className="Ticket">
                {
                    !data && (
                        <div >
                            <div>
                                <h1>Поиск билета в жизнь</h1>
                            </div>
                            <CircularProgress className="Ticket-spinner" variant="determinate" value={progress} />
                        </div>

                    )
                }
                {data && <TicketContent soulId={soulId} />}
            </div>
        </>
    );
}

export default Ticket;
