import './Ticket.css';
import { useEffect, useState } from "react";
import { CircularProgress } from "@material-ui/core";
import TicketContent from "./TicketContent";
import { useDispatch, useSelector } from "react-redux";
import { Button, Progress } from 'reactstrap';
import { useHistory } from 'react-router';
import { receiveHelpRequests, receiveRole, recieveIsMentor } from '../Store/user-types';
import { HelpRequestTable } from '../GodHelpRequests';
import { DataGrid } from "@material-ui/data-grid";
import { useMemo } from 'react';

function Ticket() {
    const history = useHistory();
    const dispatch = useDispatch();

    const soulId = useSelector(s => s.user.roleId);
    const soulStatus = useSelector(s => s.user.soulStatus);
    const token = useSelector(s => s.user.token);
    const role = useSelector(s => s.user.role);
    const isAutoSparkEnabled = useSelector(s => s.user.autoModes.find(x => x.type === "LIFE_SPARK_MODE")?.isManualMode === false);

    const [data, setData] = useState();
    const lifeSparkRequests = useSelector(x => x.user.helpRequests);
    const [personalProgram, setPersonalProgram] = useState();

    const [progress, setProgress] = useState(0);
    useEffect(() => {
        const allowed = !!token && (role === "MENTOR" || (role === "SOUL" && (soulStatus === "UNBORN" || soulStatus === "DEAD")))
        if (!allowed) {
            history.push('/')
        }
    }, [token, role, soulStatus, history]);

    useEffect(() => {
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

            const timer2 = setInterval(async () => {
                await load();
            }, 10000);

            return () => {
                clearInterval(timer2);
            };
        };
    }, [role, token, dispatch])

    useEffect(() => {
        let isCancelled = false;

        if (soulStatus === "UNBORN") {
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
            }, 10000);

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

            const loadLifeSparks = async () => {
                const response = await fetch(`http://localhost:8080/api/souls/my-requests/life-spark`, {
                    headers: { 'soul-token': token }
                });
                response.json().then(x => {
                    if (x.some(y => lifeSparkRequests?.find(z => z.id === y.id)?.status !== y.status)) {
                        dispatch(receiveHelpRequests(x));
                    }
                });
            }
            const timer3 = setInterval(async () => {
                await loadLifeSparks();
            }, 5000);
            return () => {
                clearInterval(timer);
                clearInterval(timer2);
                clearInterval(timer3);
                isCancelled = true;
            };
        }
    }, [data, token, dispatch, history, lifeSparkRequests, personalProgram, soulStatus]);

    const handleBecomeMentor = async () => {
        const response = await fetch(`http://localhost:8080/api/souls/update/mentor/true`, {
            method: 'PUT',
            headers: { 'soul-token': token }
        });
        if (response.ok) {
            dispatch(recieveIsMentor(true));
            dispatch(receiveRole("MENTOR"));
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
                    <div className="Ticket" style={{ paddingTop: '15px' }}>
                        {
                            soulStatus === "DEAD"
                                ? <div style={{ width: 'fit-content', margin: 'auto' }}>
                                    {"Вы мертвы... Все, что вам теперь остается, это"}
                                    <Button onClick={handleBecomeMentor}>Стать наставником</Button>
                                </div>
                                :
                                <>
                                    {!isAutoSparkEnabled && !lifeSparkRequests?.length && !personalProgram &&
                                        <div style={{ width: 'fit-content', margin: 'auto' }}>
                                            <Button onClick={handleMentorRequest}>Найти наставника</Button>
                                            <Button onClick={handlePersonalProgram}>Использовать персональную программу</Button>
                                        </div>
                                    }
                                    <div style={{ display: 'block' }}>
                                        {
                                            lifeSparkRequests?.length === 1 &&
                                            <div style={{ width: 'fit-content', margin: 'auto' }}>
                                                {
                                                    lifeSparkRequests.some(x => x.status === "FINISHED") ? "Получена искра жизни с помощью наставника. Ожидайте билет в жизнь..."
                                                        : `Существует запрос на поиск искры жизни с помощью наставника. Ожидайте отклик наставника.
                                                            Текущий статус: ${lifeSparkRequests.find(x => x.status !== "FINISHED")?.status}`}
                                            </div>
                                        }
                                        {
                                            !!personalProgram && <PersonalProgram personalProgram={personalProgram} handleClick={handleExercise} />
                                        }
                                    </div>
                                    {!data && <>
                                        <br />
                                        <br />
                                        <div style={{ display: 'block', width: 'fit-content', margin: 'auto' }} >

                                            <div>
                                                <h1>Поиск билета в жизнь</h1>
                                            </div>
                                            <CircularProgress className="Ticket-spinner" variant="determinate" value={progress} />
                                        </div>

                                    </>}
                                </>
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