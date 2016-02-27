import React from 'react'
import { Route } from 'react-router'
import validateVisualizer from '../validateVisualizer'

export const MODULE_PATH = 'datacube';

const Page = validateVisualizer(() => <div>DataCube Visualizer</div>, MODULE_PATH);

export default function createRoutes(dispatch) {
  return (
    <Route component={Page} path={MODULE_PATH} key={MODULE_PATH} />
  );
}

createRoutes.path = MODULE_PATH;