import './Home.css';
import Menu from "../Menu";


function Home() {
    return (
        <>
            <Menu />
            <div className="Home">
                <header className="Home-header">
                    <h1>Добро пожаловать в приложение Душа!</h1>
                    <h2 className="Home-subtitle">Что может наш сервис?</h2>
                    <p className="Home-firstParagraph">Мы автоматизируем процессы управления душами, находящимися вне земных тел.</p>
                    <p className="Home-secondParagraph">Мы позволяет душам отправлять различные запросы, а различным потусторонним агентам - отслеживать эти запросы.</p>
                    <p className="Home-thirdParagraph"> Мы упрощаем агентам, совершать наиболее разумные действия в зависимости от собственных интересов.</p>
                </header>
            </div>
        </>
    );
}

export default Home;
