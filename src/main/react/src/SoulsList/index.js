import './SoulsList.css';
import { soulRows } from './mock';
import { soulColumns } from './constants';
import Menu from '../Menu';
import { DataGrid } from '@material-ui/data-grid';
import {useEffect, useState} from "react";

function SoulsList() {

    const [souls, setSouls] = useState([]);

    useEffect(() => {
        let isCancelled = false;

        const loadSouls = async () => {
            try {
                const data = await fetch("http://localhost:8080/api/souls/");
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
    }, []);

    return (
        <>
            <Menu />
            <div className="SoulsList-content">
                <h1>Спиок душ</h1>
                <div style={{ height: 400, width: '100%' }}>
                   <DataGrid rows={souls} columns={soulColumns} pageSize={5} checkboxSelection/>
                </div>

            </div>
        </>
    );
}

export default SoulsList;
