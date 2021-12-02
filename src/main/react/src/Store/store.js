import { configureStore } from "@reduxjs/toolkit";
import userReducer from '../Store/user-types';

export default configureStore({
    reducer: {
        user: userReducer
    }
})