import './TicketContent.css';
import Button from '@material-ui/core/Button';
import {useCallback, useEffect, useState} from "react";
import { useHistory } from "react-router";

function TicketContent(props) {
    let history = useHistory();
    const { soulId } = props
    const redirect = useCallback(() => {
        history.push('/');
    }, [history]);

    const [tick, setTick] = useState(10);

    useEffect(() => {
        const timer = setInterval(() => {
            setTick((prevProgress) => (prevProgress > 0 ? prevProgress - 1 : redirect()));
        }, 1000);

        return () => {
            clearInterval(timer);
        };
    }, [redirect]);

    return (
        <div className="TicketContent">
            <h2 className="TicketContent-title">Билет в жизнь</h2>
            <div className="TicketContent-content">
                <p className="TicketContent-info">{`Уважаемый(ая) ${soulId}, Вы только что получили билет в жизнь! Поздравляем Вас!`}</p>
                <p className="TicketContent-redirectInfo">{`Автоматическая отправка на Землю через ${tick} секунд`}</p>
                <Button onClick={redirect} className="TicketContent-button">Отправиться на Землю прямо сейчас</Button>
            </div>
        </div>
    );
}

export default TicketContent;
