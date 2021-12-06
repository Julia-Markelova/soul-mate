import { createSlice } from "@reduxjs/toolkit";

export const initialState = {
    role: '',
    token: '',
    userId: '',
    roleId: '',
    soulStatus: '',
    isMentor: false,
    helpRequests: undefined,
    autoModes: []
}
export const userSlice = createSlice({
    name: 'user',
    initialState: initialState,
    reducers: {
        clearState: (state, action) => {
            state.role = '';
            state.token = '';
            state.userId = '';
            state.roleId = '';
            state.soulStatus = '';
            state.isMentor = false;
            state.helpRequests = undefined;
            state.autoModes = [];
        },
        receiveRole: (state, action) => {
            state.role = action.payload
        },
        receiveUserId: (state, action) => {
            state.userId = action.payload
        },
        receiveRoleId: (state, action) => {
            state.roleId = action.payload
        },
        recieveIsMentor: (state, action) => {
            state.isMentor = action.payload
        },
        receiveToken: (state, action) => {
            state.token = action.payload
        },
        receiveHelpRequests: (state, action) => {
            state.helpRequests = action.payload
        },
        receiveSoulStatus: (state, action) => {
            state.soulStatus = action.payload
        },
        receiveAutoModes: (state, action) => {
            state.autoModes = action.payload
        },
        receiveAutoMode: (state, action) => {
            const item = action.payload;
            state.autoModes = [...state.autoModes.filter(x => x.id !== item.id), item]
        },
        receiveHelpRequest: (state, action) => {
            const item = action.payload;
            state.helpRequests = [...state.helpRequests.filter(x => x.id !== item.id), item]
        },
        removeHelpRequest: (state, action) => {
            state.helpRequests = [...state.helpRequests.filter(x => x.id !== action.payload)]
        }
    }
});

export default userSlice.reducer

export const { receiveRole, receiveToken, receiveHelpRequests, recieveIsMentor, clearState,
    removeHelpRequest, receiveUserId, receiveHelpRequest, receiveRoleId, receiveAutoModes, receiveAutoMode,
    receiveSoulStatus } = userSlice.actions