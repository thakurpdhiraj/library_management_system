import resource from "../resource/resource";

export const getUsersOrder = async () => {
  let response = await resource.get("/orders/users/me");
  if (!response.status === 200) {
    throw new Error(response.data);
  } else {
    return await response.data;
  }
};

export const getUsersOrderHistory = async () => {
  let response = await resource.get("/orders/users/me/history");
  if (!response.status === 200) {
    throw new Error(response.data);
  } else {
    return await response.data;
  }
};
