export const sendResponse = (response, status) => {
  if (response.status !== status) {
    throw new Error(response.data);
  } else {
    return response.data;
  }
};
