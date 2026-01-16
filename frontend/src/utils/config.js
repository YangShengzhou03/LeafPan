import Server from './Server'

const defaultConfig = {
  maxFileSize: 5120,
  appName: 'LeafPan System',
  apiVersion: 'v1'
}

let currentConfig = { ...defaultConfig }

export const getConfig = () => {
  return { ...currentConfig }
}

export const getConfigItem = (key, defaultValue = null) => {
  return currentConfig[key] !== undefined ? currentConfig[key] : defaultValue
}

export const loadConfig = async () => {
  try {
    const response = await Server.get('/config')
    if (response && response.data) {
      currentConfig = { ...defaultConfig, ...response.data }
      return currentConfig
    }
  } catch (error) {
  }
  
  return { ...defaultConfig }
}

export const setConfigItem = (key, value) => {
  currentConfig[key] = value
}

export const resetConfig = () => {
  currentConfig = { ...defaultConfig }
}

loadConfig().catch(() => {})

export default {
  getConfig,
  getConfigItem,
  loadConfig,
  setConfigItem,
  resetConfig
}
