import './SoulsList.css';
import { soulRows } from './mock';
import { soulColumns } from './constants';
import Menu from '../Menu';
import { DataGrid } from '@material-ui/data-grid';


function SoulsList() {
    return (
        <>
            <Menu />
            <div className="SoulsList-content">
                <h1>Спиок душ</h1>
                <div style={{ height: 400, width: '100%' }}>
                    <DataGrid rows={soulRows} columns={soulColumns} pageSize={5} checkboxSelection />
                </div>

            </div>
        </>
    );
}

export default SoulsList;
