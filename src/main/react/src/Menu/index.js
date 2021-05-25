import './Menu.css';
import { Link } from "react-router-dom";


function Menu() {
    return (
        <div className="Menu">
            <h3>
                <Link to="/">На главную</Link>
            </h3>
            <h3>
                <Link to="/souls">Список душ</Link>
            </h3>
            <h3>
                <Link to="/parents">Для родственников</Link>
            </h3>
            <h3>
                <Link to="/ticket">Билет в жизнь</Link>
            </h3>
        </div>
    );
}

export default Menu;
