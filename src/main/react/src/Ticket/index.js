import './Ticket.css';
import { useEffect, useState } from "react";
import { CircularProgress } from "@material-ui/core";
import TicketContent from "./TicketContent";
import { useDispatch, useSelector } from "react-redux";
import { Button, Progress } from 'reactstrap';
import { useHistory } from 'react-router';
import { receiveHelpRequests, recieveIsMentor } from '../Store/user-types';
import { HelpRequestTable } from '../GodHelpRequests';
import { DataGrid } from "@material-ui/data-grid";
import { useMemo } from 'react';


function Ticket() {
    const history = useHistory();
    const dispatch = useDispatch();

    const soulId = useSelector(s => s.user.roleId);
    const isMentor = useSelector(s => s.user.isMentor);
    const soulStatus = useSelector(s => s.user.soulStatus);
    const token = useSelector(s => s.user.token);
    const role = useSelector(s => s.user.role);
    const isAutoTicketEnabled = useSelector(s => s.user.autoModes.find(x => x.type === "LIFE_TICKET_MODE")?.isManualMode === false);

    const [data, setData] = useState();
    const lifeSparkRequests = useSelector(x => x.user.helpRequests);
    const [personalProgram, setPersonalProgram] = useState();

    const [progress, setProgress] = useState(0);

    useEffect(() => {
        const allowed = !!token && (role === "MENTOR" || (role === "SOUL" && (soulStatus === "UNBORN" || soulStatus === "DEAD")))
        if (!allowed) {
            history.push('/')
        }

        if (role === "MENTOR") {
            const load = async () => {
                const response = await fetch(`http://localhost:8080/api/mentors/open-requests`, {
                    headers: { 'soul-token': token }
                })

                if (response.ok) {
                    response.json().then(x => dispatch(receiveHelpRequests(x)));
                }
            }
            load();

            return
        };
        let isCancelled = false;

        const loadSouls = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/souls/life-tickets`, {
                    headers: { 'soul-token': token }
                });
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

        if (!isAutoTicketEnabled) {
            if (!personalProgram) {
                const load = async () => {
                    const response = await fetch(`http://localhost:8080/api/souls/personal-program`, {
                        headers: { 'soul-token': token }
                    });
                    if (response.ok) {
                        response.json().then(x => setPersonalProgram(x));
                    }
                }
                load();
            }

            if (!lifeSparkRequests) {
                const load = async () => {
                    const response = await fetch(`http://localhost:8080/api/souls/my-requests/life-spark`, {
                        headers: { 'soul-token': token }
                    });
                    response.json().then(x => dispatch(receiveHelpRequests(x)));
                }
                load();
            }
        }
        return () => {
            clearInterval(timer);
            clearInterval(timer2);
            isCancelled = true;
        };
    }, [data, token]);

    const handleBecomeMentor = async () => {
        const response = await fetch(`http://localhost:8080/api/souls/update/mentor/true`, {
            method: 'PUT',
            headers: { 'soul-token': token }
        });
        if (response.ok) {
            dispatch(recieveIsMentor(true));
        }
    }

    const handleMentorRequest = async () => {
        const response = await fetch(`http://localhost:8080/api/souls/create-help-request/life-spark`, {
            method: 'POST',
            headers: { 'soul-token': token }
        });
        if (response.ok) {
            response.json().then(x => dispatch(receiveHelpRequests([x])));
        }
    }

    const handlePersonalProgram = async () => {
        const response = await fetch(`http://localhost:8080/api/souls/personal-program`, {
            method: 'POST',
            headers: { 'soul-token': token }
        });
        if (response.ok) {
            response.json().then(x => setPersonalProgram(x));
        }
    }

    const handleExercise = async (exerciseId, progress) => {
        const response = await fetch(`http://localhost:8080/api/souls/personal-program/update-exercise-progress/${exerciseId}?progress=${progress}`, {
            method: 'PUT',
            headers: { 'soul-token': token }
        });
        if (response.ok) {
            response.json().then(x => setPersonalProgram(x));
        }
    }
    return (
        <>
            {
                role === "MENTOR"
                    ? <HelpRequestTable
                        onAcceptUrl="http://localhost:8080/api/mentors/accept-request/"
                        onFinishUrl="http://localhost:8080/api/mentors/finish-request/"
                        onRejectUrl="http://localhost:8080/api/mentors/reject-request/"
                    />
                    :
                    <div className="Ticket" style={{ display: !personalProgram ? 'flex' : 'block' }}>
                        {
                            soulStatus === "DEAD"
                                ? <>
                                    {"Вы мертвы... Все, что вам теперь остается, это"}
                                    <Button onClick={handleBecomeMentor}>Стать наставником</Button>
                                </>
                                :
                                isAutoTicketEnabled ?
                                    !data && (
                                        <div >
                                            <div>
                                                <h1>Поиск билета в жизнь</h1>
                                            </div>
                                            <CircularProgress className="Ticket-spinner" variant="determinate" value={progress} />
                                        </div>

                                    )
                                    : (
                                        <>
                                            {!lifeSparkRequests?.length && !personalProgram &&
                                                <>
                                                    <Button onClick={handleMentorRequest}>Найти наставника</Button>
                                                    <Button onClick={handlePersonalProgram}>Использовать персональную программу</Button>
                                                </>
                                            }
                                            {
                                                lifeSparkRequests?.length === 1 &&
                                                <>
                                                    {`Существует запрос на поиск искры жизни с помощью наставника. Ожидайте отклик наставника.
                                                    Текущий статус: ${lifeSparkRequests.find(x => x.status !== "FINISHED")?.status}`}
                                                </>
                                            }
                                            {
                                                !!personalProgram && <div style={{ display: 'block' }}> <PersonalProgram personalProgram={personalProgram} handleClick={handleExercise} /></div>
                                            }
                                        </>
                                    )
                        }
                        {data && <TicketContent soulId={soulId} />}
                    </div>
            }
        </>
    );
}

export default Ticket;


const PersonalProgram = (props) => {
    const { personalProgram, handleClick } = props;

    const columns = [
        {
            field: 'skill',
            headerName: 'Навык',
            width: 300
        }, {
            field: 'progress',
            headerName: 'Прогресс',
            width: 300,
            renderCell: params => <Progress color="success" value={Math.min(100, params.row.progress)} style={{ width: "100%" }} />
        },
        {
            field: 'id',
            headerName: 'Действие',
            width: 300,
            renderCell: params => {
                const onClick = async e => {
                    e.stopPropagation();
                    await handleClick(params.row.id, Math.min(100, params.row.progress + 10))
                }

                return params.row.progress < 100 ? <Button onClick={onClick}>Тренировать</Button> : "Завершено"
            }
        }
    ]

    const sorted = useMemo(() => {
        return [...personalProgram.exercises].sort((a, b) => a.skill.localeCompare(b.skill))
    }, [personalProgram])

    return <div style={{ height: 'auto', minHeight: '600px', marginTop: '30px' }}>
        <DataGrid rows={sorted} columns={columns} pageSize={10} autoHeight />
    </div>
}