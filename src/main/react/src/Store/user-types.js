import { createSlice } from "@reduxjs/toolkit";

export const userSlice = createSlice({
    name: 'user',
    initialState: {
        role: '',
        token: '',
        userId: '',
        helpRequests: []
    },
    reducers: {
        receiveRole: (state, action) => {
            state.role = action.payload
        },
        receiveUserId: (state, action) => {
            state.userId = action.payload
        },
        receiveToken: (state, action) => {
            state.token = action.payload
        },
        receiveHelpRequests: (state, action) => {
            state.helpRequests = action.payload
        },
        receiveHelpRequest: (state, action) => {
            const item = action.payload;
            state.helpRequests = [...state.helpRequests.filter(x => x.id !== item.id), item]
        }
    }
});

export default userSlice.reducer

export const { receiveRole, receiveToken, receiveHelpRequests, receiveUserId, receiveHelpRequest } = userSlice.actions