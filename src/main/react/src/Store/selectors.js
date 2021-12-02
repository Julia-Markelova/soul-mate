import { createSelector } from "reselect";

const getState = state => state;
const getToken = createSelector(
    [getState]
)