import './Ticket.css';
import Menu from "../Menu";
import { useEffect, useState } from "react";
import { CircularProgress } from "@material-ui/core";
import TicketContent from "./TicketContent";


function Ticket() {
    const [isLoading, setIsLoading ] = useState(true);

    const [progress, setProgress] = useState(0);

    useEffect(() => {
        const timer = setInterval(() => {
            setProgress((prevProgress) => (prevProgress >= 100 ? setIsLoading(false) : prevProgress + 10));
        }, 800);

        return () => {
            clearInterval(timer);
        };
    }, []);

    return (
        <>
            <Menu />
            <div className="Ticket">
                {
                    isLoading && (
                        <div >
                            <div>
                                <h1>Поиск билета в жизнь</h1>
                            </div>
                            <CircularProgress className="Ticket-spinner" variant="determinate" value={progress} />
                        </div>

                    )
                }
                {!isLoading && <TicketContent />}
            </div>
        </>
    );
}

export default Ticket;
