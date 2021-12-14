import './Home.css';

function Home() {
    return (
        <>
            <div className="Home">
                <header className="Home-header">
                    <h1>Добро пожаловать в приложение Душа!</h1>
                    <h2 className="Home-subtitle">Что может наш сервис?</h2>
                    <p className="Home-firstParagraph">Мы автоматизируем процессы управления душами, находящимися вне земных тел.</p>
                    <p className="Home-secondParagraph">Мы позволяет душам отправлять различные запросы, а различным потусторонним агентам - отслеживать эти запросы.</p>
                    <p className="Home-thirdParagraph"> Мы упрощаем агентам принятие решений в зависимости от их собственных интересов.</p>
                </header>
            </div>
        </>
    );
}

export default Home;
