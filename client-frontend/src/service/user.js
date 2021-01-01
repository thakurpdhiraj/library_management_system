import resource from "../resource/resource";

export const getAllUsers = async () => {
  let response = await resource.get("/admin/users");
  if (!response.status === 200) {
    throw new Error(response.data);
  } else {
    return await response.data;
  }
};
