import './TicketContent.css';
import Button from '@material-ui/core/Button';
import {useEffect, useState} from "react";
import { useHistory } from "react-router";

const mockId = 394950;

function TicketContent() {
    let history = useHistory();

    const redirect = () => {
        history.push('/');
    };

    const [tick, setTick] = useState(10);

    useEffect(() => {
        const timer = setInterval(() => {
            setTick((prevProgress) => (prevProgress > 0 ? prevProgress - 1 : redirect()));
        }, 1000);

        return () => {
            clearInterval(timer);
        };
    }, []);

    return (
        <div className="TicketContent">
            <h2 className="TicketContent-title">Билет в жизнь</h2>
            <div className="TicketContent-content">
                <p className="TicketContent-info">{`Уважаемый(ая) id${mockId}, Вы только что получили билет в жизнь! Поздравляем Вас!`}</p>
                <p className="TicketContent-redirectInfo">{`Автоматическая отправка на Землю через ${tick} секунд`}</p>
                <Button onClick={redirect} className="TicketContent-button">Отправиться на Землю прямо сейчас</Button>
            </div>
        </div>
    );
}

export default TicketContent;