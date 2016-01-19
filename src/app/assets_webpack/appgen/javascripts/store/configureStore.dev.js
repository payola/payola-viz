import { createStore, applyMiddleware, compose, combineReducers } from 'redux'
import { syncHistory, routeReducer } from 'redux-simple-router'
import { browserHistory } from 'react-router'
import DevTools from '../containers/DevTools'
// import thunk from 'redux-thunk'
// import api from '../middleware/api'
import createLogger from 'redux-logger'
// import rootReducer from '../reducers'

const rootReducer = combineReducers({
  routing: routeReducer
});

const reduxRouterMiddleware = syncHistory(browserHistory);

const finalCreateStore = compose(
//  applyMiddleware(thunk, api),
  applyMiddleware(reduxRouterMiddleware),
  applyMiddleware(createLogger()),
  DevTools.instrument()
)(createStore);


export default function configureStore(initialState) {
  const store = finalCreateStore(rootReducer, initialState);

  // Required for replaying actions from devtools to work
  reduxRouterMiddleware.listenForReplays(store);

  /*
  if (module.hot) {
    // Enable Webpack hot module replacement for reducers
    module.hot.accept('../reducers', () => {
      const nextRootReducer = require('../reducers')
      store.replaceReducer(nextRootReducer)
    })
  }
  */

  return store
}