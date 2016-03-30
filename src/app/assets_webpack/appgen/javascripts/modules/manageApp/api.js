import rest from '../../misc/rest'

/**  @returns {Promise<object>} application */
export async function getApplication(id) {
  const result = await rest('app/getApp/' + id, {});
  return result.data.application;
}

/**  @returns {Promise<{status, message, data}>} */
export async function updateSettings(id, values) {
  return await rest('manageApp/updateAppSettings/' + id, values);
}

/**  @returns {Promise<{status, message, data}>} */
export async function saveConfiguration(id, configuration) {
  return await rest('manageApp/saveAppConfiguration/' + id, {
    configuration: JSON.stringify(configuration)
  });
}

/**  @returns {Promise<{status, message, data}>} */
export async function publishApp(id, published) {
  return await rest('manageApp/publishApp/' + id, { published });
}

/**  @returns {Promise<object>} */
export async function getConfiguration(id) {
  const result = await rest('app/getAppConfiguration/' + id, {});
  return JSON.parse(result.data.configuration) || {};
}
