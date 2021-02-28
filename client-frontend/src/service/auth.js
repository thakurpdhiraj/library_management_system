import resource from "@/resource/resource";
import * as util from "@/util/authUtil";

export const login = async cred => {
  let response = await resource.post(
    "/login",
    new URLSearchParams({
      username: cred.username,
      password: cred.password
    })
  );

  if (!response.status === 200) {
    throw new Error(response.data);
  } else {
    let user = response.data;
    util.setSessionUser(user);
    return user;
  }
};

export const logout = async () => {
  let response = await resource.post("/logout");

  if (response.status !== 200) {
    throw new Error(response.data);
  } else {
    util.removeSessionUser();
  }
};
