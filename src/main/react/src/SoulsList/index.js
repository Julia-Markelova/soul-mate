import './SoulsList.css';
import { soulColumns } from './constants';
import { DataGrid } from '@material-ui/data-grid';
import {useEffect, useMemo, useState} from "react";
import {Input} from "@material-ui/core";
import { useHistory } from 'react-router';
import * as React from "react";
import {  useSelector } from "react-redux";

function SoulsList() {

    const history = useHistory();

    const role = useSelector(s => s.user.role);
    const token = useSelector(s => s.user.token);

    useEffect(() => {
        if (role !== "ADMIN" || !token) {
            history.push('/')
        }
    }, [role, history, token]);

    const [souls, setSouls] = useState([]);
    const [filterValue, setFilterValue] = useState("");

    useEffect(() => {
        let isCancelled = false;

        const loadSouls = async () => {
            try {
                const data = await fetch("http://localhost:8080/admin/souls/", { headers: { 'soul-token': token }});
                !isCancelled && data.json().then(x => setSouls(x));
            }
            catch (error) {
                !isCancelled && console.log(error.toString());
            }
        };

        loadSouls();

        return () => {
            isCancelled = true;
        }
    }, [token]);

    const filteredSouls = useMemo(() => {
        const filter = filterValue.toLowerCase();
        return souls.filter(x => x.id?.toLowerCase().includes(filter) || x.type?.toLowerCase().includes(filter));
    }, [filterValue, souls])

    return (
        <>
            <div className="SoulsList-content">
                <h1>Список душ</h1>

                <Input
                    style={{border: '1px solid', padding: '5px 10px'}}
                    placeholder="Поиск по id или типу"
                    value={filterValue}
                    onChange={e => setFilterValue(e.target.value)}
                />
                {
                        <div style={{ height: 400, width: '100%' }}>

                            {!!filteredSouls?.length
                                ?
                                <DataGrid rows={filteredSouls} columns={soulColumns} pageSize={5} checkboxSelection/>
                                : <div style={{fontSize: 'large', margin: 'auto', textAlign: 'center'}}>
                                    Не обнаружено душ
                            </div>
                            }
                        </div>
                }
                <strong style={{fontSize: 'large'}}>
                    Всего душ: {souls.length}
                </strong>
            </div>
        </>
    );
};

export default SoulsList;
