import { createSlice } from "@reduxjs/toolkit";

export const userSlice = createSlice({
    name: 'user',
    initialState: {
        role: '',
        token: '',
        userId: '',
        roleId: '',
        soulStatus: '',
        helpRequests: []
    },
    reducers: {
        receiveRole: (state, action) => {
            state.role = action.payload
        },
        receiveUserId: (state, action) => {
            state.userId = action.payload
        },
        receiveRoleId: (state, action) => {
            state.roleId = action.payload
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

export const { receiveRole, receiveToken, receiveHelpRequests,
    removeHelpRequest, receiveUserId, receiveHelpRequest, receiveRoleId,
receiveSoulStatus } = userSlice.actions